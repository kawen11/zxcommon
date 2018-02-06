package com.zxj.common.http;

import com.zxj.common.config.HttpClientConf;
import com.zxj.common.config.StringPool;
import com.zxj.common.utils.IOUtils;
import com.zxj.common.utils.StringUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.*;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClientUtils
 */
class HttpClientUtils {
    /**
     * 禁止实例化
     */
    private HttpClientUtils() {
        //disable instance
    }

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * connManager
     */
    private static PoolingHttpClientConnectionManager connManager = null;


    static {
        try {
            // SSLContext
            SSLContextBuilder sslContextbuilder = new SSLContextBuilder();
            SSLContext sslContext = sslContextbuilder.loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    .register("https", new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.getDefaultHostnameVerifier())).build();

            // Create ConnectionManager
            connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            // Create socket configuration
            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
            connManager.setDefaultSocketConfig(socketConfig);

            // Create message constraints
            MessageConstraints messageConstraints = MessageConstraints.custom()
                    .setMaxHeaderCount(1000)
                    .setMaxLineLength(100 * 1024 * 1024)
                    .build();

            // Create connection configuration
            ConnectionConfig connectionConfig = ConnectionConfig.custom()
                    .setMalformedInputAction(CodingErrorAction.IGNORE)
                    .setUnmappableInputAction(CodingErrorAction.IGNORE)
                    .setMessageConstraints(messageConstraints)
                    .build();

            connManager.setDefaultConnectionConfig(connectionConfig);
            connManager.setMaxTotal(HttpClientConf.getPoolingHttpClientConnectionManagerMaxTotal());
            connManager.setDefaultMaxPerRoute(HttpClientConf.getPoolingHttpClientConnectionManagerDefaultMaxPerRoute());
        } catch (KeyManagementException e) {
            logger.error("KeyManagementException", e);
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException", e);
        } catch (Exception e) {
            logger.error("Exception", e);
        }

    }


    /**
     * 获取response
     *
     * @param reqPack RequestPackage
     * @return ResponsePackage
     */
    static ResponsePackage exec(RequestPackage reqPack) {
        HttpRequestBase requestBase = createHttpRequestBase(reqPack);
        if (requestBase == null) //如果不支持的 method, 直接返回 null
            return null;
        HttpResponse httpResponse = null;
        try {
            //从连接池创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClients.custom().disableRedirectHandling().setConnectionManager(connManager);
            // client
            CloseableHttpClient client = httpClientBuilder.build();
            // 执行请求
            httpResponse = client.execute(requestBase, reqPack.getHttpContext());
            // 获取响应消息实体
            return getResponsePackage(httpResponse, reqPack);
        } catch (Exception e) {
            logger.error("Request {} Exception", reqPack.getUrl(), e);
        } finally {
            if (httpResponse != null) {
                try {
                    EntityUtils.consume(httpResponse.getEntity());
                } catch (IOException e) {
                    logger.error("释放资源出错", e);
                }
            }
            requestBase.releaseConnection();
        }
        return null;
    }


    /**
     * 设置 http post
     *
     * @param reqPack RequestPackage
     * @return HttpRequestBase
     */
    public static HttpRequestBase createHttpRequestBase(RequestPackage reqPack) {
        HttpRequestBase requestBase;
        if ("POST".equalsIgnoreCase(reqPack.getMethod())) {
            //post 方法，创建 httpPost
            HttpPost post = new HttpPost(reqPack.getUrl());
            if (reqPack.getPostContent() != null) {
                //request post content 不为空
                post.setEntity(new StringEntity(reqPack.getPostContent(), reqPack.getCharset()));
            } else if (reqPack.getNameValuePairs() != null) {
                //request data不为空 则创建UrlEncodedFormEntity
                post.setEntity(new UrlEncodedFormEntity(reqPack.getNameValuePairs(), reqPack.getCharset()));
            }
            requestBase = post;
        } else if ("GET".equalsIgnoreCase(reqPack.getMethod())) {
            //exec 方法
            String fullUrl = reqPack.getUrl();
            if (reqPack.getNameValuePairs() != null) {
                //如果原url包含问号，则追加&  不包含问号，则追加问号
                StringBuilder urlBuffer = new StringBuilder(fullUrl).append(
                        fullUrl.contains(StringPool.Symbol.QUESTION) ?
                                StringPool.Symbol.AMPERSAND :
                                StringPool.Symbol.QUESTION
                );
                urlBuffer.append(URLEncodedUtils.format(reqPack.getNameValuePairs(), reqPack.getCharset()));
                fullUrl = urlBuffer.toString();
            }
            requestBase = new HttpGet(fullUrl);
        } else {
            logger.error("HTTP METHOD UNSUPPORTED");
            return null;
        }
        if (reqPack.getHeaders() != null && !reqPack.getHeaders().isEmpty()) {
            for (Map.Entry<String, String> kv : reqPack.getHeaders().entrySet()) {
                requestBase.setHeader(kv.getKey(), kv.getValue());
            }
        }
        HttpClientContext context = HttpClientContext.create();
        reqPack.setHttpContext(context);

        //设置cookie的第二种方式 1) header cookie  2) BasicCookieStore
        if (reqPack.getCookies() != null) {
            BasicCookieStore cookieStore = new BasicCookieStore();
            String host = requestBase.getURI().getHost();
            for (Map.Entry<String, String> kv : reqPack.getCookies().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(kv.getKey(), kv.getValue());
                cookie.setPath("/");
                cookie.setDomain(host);
                cookieStore.addCookie(cookie);
            }
            context.setCookieStore(cookieStore);
        }
        // set accept
        if (StringUtils.isNotBlank(reqPack.getAccept())) {
            requestBase.setHeader("Accept", reqPack.getAccept());
        }
        // set User-Agent
        String ua = reqPack.getUserAgent();
        if (StringUtils.isBlank(ua)) {
            //设置默认 UA
            ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";
        }
        requestBase.setHeader("User-Agent", ua);
        //设置 timeout
        RequestConfig requestConfig = reqPack.getRequestConfig();
        if (requestConfig == null) {
            requestConfig = RequestConfig.custom()
                    .setSocketTimeout(HttpClientConf.getHttpClientSocketTimeout()) //设置传输超时
                    .setConnectTimeout(HttpClientConf.getHttpClientConnectTimeout())//设置从pool中获取client的超时时间
                    .setConnectionRequestTimeout(HttpClientConf.getHttpClientRequestTimeout())
                    .build();//设置连接超时时间
        }
        requestBase.setConfig(requestConfig);
        return requestBase;
    }

    public static ResponsePackage getResponsePackage(HttpResponse httpResponse, RequestPackage requestPackage) {
        try {
            ResponsePackage respPack = new ResponsePackage();
            HttpEntity entityRep = httpResponse.getEntity();
            if (entityRep != null) {
                // 获取HTTP响应的状态码
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                respPack.setHttpStatus(statusCode);
                if (statusCode == HttpStatus.SC_OK) {
                    //只有正常返回的请求才有正文内容
                    String responseContent = IOUtils.stream2String(entityRep.getContent(), requestPackage.getCharset());
                    respPack.setContent(responseContent);
                    //处理header
                    Header[] allHeaders = httpResponse.getAllHeaders();
                    if (allHeaders != null && allHeaders.length > 0) {
                        HashMap<String, String> map = new HashMap<>();
                        for (Header header : allHeaders) {
                            map.put(header.getName(), header.getValue());
                        }
                        respPack.setHeaders(map);
                    }
                } else {
                    //如果是调试模式, response 的内容会被赋值
                    if (logger.isDebugEnabled()) {
                        String responseContent = IOUtils.stream2String(entityRep.getContent(), requestPackage.getCharset());
                        respPack.setContent(responseContent);
                        logger.debug("REQUEST:{}\r\nCODE:{}\r\nRESPONSE:{}", requestPackage.getUrl(), statusCode, responseContent);
                    }
                }
            }
            return respPack;
        } catch (Exception ex) {
            logger.error("访问url {}出错", requestPackage.getUrl(), ex);
            return null;
        }
    }
}
