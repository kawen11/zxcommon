package com.zxj.common.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zxj.common.config.HttpClientConf;
import com.zxj.common.utils.JsonUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RequestPackage
 *
 * @author 
 */
public class RequestPackage {

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(RequestPackage.class);

    /**
     * 请求url
     */
    private String url;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求头
     */
    private Map<String, String> headers;

    /**
     * 请求的cookie
     */
    private Map<String, String> cookies;

    /**
     * keyValueMap 的数据
     */
    private List<NameValuePair> nameValuePairs;

    /**
     * post的内容
     */
    private String postContent;

    /**
     * accept
     */
    private String accept;

    /**
     * user agent
     */
    private String userAgent;

    /**
     * 默认编码 utf8
     */
    private String charset = "UTF-8";

    /**
     * 读取 response socket time out, 单位毫秒
     */
    private int soTimeout = 0;

    /**
     * httpContext
     */
    private HttpContext httpContext;

    /**
     * Request config
     */
    private RequestConfig requestConfig;

    /**
     * ResponsePackage
     */
    private ResponsePackage responsePackage;

    /**
     * 获取超时时间
     *
     * @return
     */
    public Integer getSoTimeout() {
        return soTimeout;
    }

    /**
     * 设置超时时间
     *
     * @param soTimeout 单位毫秒
     */
    public RequestPackage setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
        this.requestConfig = RequestConfig.custom()
                .setSocketTimeout(soTimeout) //设置传输超时
                .setConnectTimeout(HttpClientConf.getHttpClientConnectTimeout())//设置从pool中获取client的超时时间
                .setConnectionRequestTimeout(HttpClientConf.getHttpClientRequestTimeout())
                .build();
        return this;
    }

    /**
     * 获取请求地址
     *
     * @return 请求地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     * @return this
     */
    public RequestPackage setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 获取方法(目前只支持 exec|post)
     *
     * @return 方法名
     */
    public String getMethod() {
        return method;
    }

    /**
     * 设置方法
     *
     * @param method 方法
     * @return
     */
    public RequestPackage setMethod(String method) {
        this.method = method;
        return this;
    }

    /**
     * 获取头部
     *
     * @return 头部的键值对
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * 设置头部
     *
     * @param headers header 键值对
     * @return this
     */
    public RequestPackage setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 获取 cookie
     *
     * @return cookie 键值对
     */
    public Map<String, String> getCookies() {
        return cookies;
    }

    /**
     * 设置 cookie
     *
     * @param cookies cookie 键值对
     * @return this
     */
    public RequestPackage setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
        return this;
    }

    /**
     * 对应 StringEntity 的内容
     *
     * @return string
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * 设置 StringEntity 的内容
     *
     * @param postContent 内容
     * @return this
     */
    public RequestPackage setPostContent(String postContent) {
        this.postContent = postContent;
        return this;
    }

    /**
     * 获取 accept
     *
     * @return
     */
    public String getAccept() {
        return accept;
    }

    /**
     * 设置 Accept (很少使用)
     *
     * @param accept String
     * @return this
     */
    public RequestPackage setAccept(String accept) {
        this.accept = accept;
        return this;
    }

    /**
     * 设置 ua
     *
     * @return user agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * 设置 ud
     *
     * @param userAgent useragent
     * @return this
     */
    public RequestPackage setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public RequestPackage setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
        return this;
    }

    /**
     * 直接设置 post
     *
     * @param map post的键值对
     * @return this
     */
    public RequestPackage post(Map<?, ?> map) {
        this.method = "POST";
        setNameValuePairs(map);
        return this;
    }

    /**
     * 设置请求的data
     *
     * @param map 设置 body 的键值对
     * @return this
     */
    public RequestPackage setNameValuePairs(Map<?, ?> map) {
        if (map != null && !map.isEmpty()) {
            this.nameValuePairs = new ArrayList<>();
            for (Map.Entry<?, ?> kv : map.entrySet()) {
                if (kv.getKey() != null) {
                    String value = kv.getValue() == null ? "" : kv.getValue().toString();
                    BasicNameValuePair nvp = new BasicNameValuePair(kv.getKey().toString(), value);
                    this.nameValuePairs.add(nvp);
                }
            }
        }
        return this;
    }

    /**
     * 获取 charset
     *
     * @return charset
     */
    public Charset getCharset() {
        return Charset.forName(charset);
    }

    /**
     * 设置字符集
     *
     * @param charset 字符集
     * @return this
     */
    public RequestPackage setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    /**
     * 获取httpcontext
     *
     * @return HttpContext
     */
    public HttpContext getHttpContext() {
        return httpContext;
    }

    public void setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
    }

    /**
     * 获取请求配置
     *
     * @return RequestConfig
     */
    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setResponsePackage(ResponsePackage responsePackage) {
        this.responsePackage = responsePackage;
    }

    /**
     * 获取 response
     *
     * @return ResponsePackage
     */
    public ResponsePackage getResponse() {
        try {
            if (responsePackage == null) {
                responsePackage = HttpClientUtils.exec(this);
            }
            return responsePackage;
        } catch (Exception ex) {
            logger.error("{} == exec response error", this.getUrl(), ex);
            return null;
        }
    }

    /**
     * 直接转换成对象
     *
     * @param clazz 类型
     * @param <T>   clazz类型的实例
     * @return
     */
    public <T> T getResponse(Class<T> clazz) {
        ResponsePackage response = getResponse();
        if (response == null) {
            return null;
        }
        if (response.isSuccess()) {
            return JsonUtils.string2Obj(response.getContent(), clazz);
        }
        return null;
    }

    /**
     * 直接转换成对象
     *
     * @param typeReference 类型
     * @param <T>           typeReference类型的实例
     * @return T
     */
    public <T> T getResponse(TypeReference<T> typeReference) {
        ResponsePackage response = getResponse();
        if (response == null) {
            return null;
        }
        if (response.isSuccess()) {
            return JsonUtils.string2Obj(response.getContent(), typeReference);
        }
        return null;
    }

    /*************简化语法,创建实例***************/

    /**
     * 直接创建 request package exec
     *
     * @param url url 地址
     * @return RequestPackage
     */
    public static RequestPackage get(String url) {
        return new RequestPackage()
                .setUrl(url)
                .setMethod("GET");
    }

    /**
     * 直接创建 request package post
     *
     * @param url url 地址
     * @return RequestPackage
     */
    public static RequestPackage post(String url, Map<?, ?> data) {
        return new RequestPackage()
                .setUrl(url)
                .setMethod("POST")
                .setNameValuePairs(data);
    }

    /**
     * 直接创建 request package post
     *
     * @param url url 地址
     * @return RequestPackage
     */
    public static RequestPackage post(String url) {
        return post(url, null);
    }
}
