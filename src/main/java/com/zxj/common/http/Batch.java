package com.zxj.common.http;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.fibers.httpclient.FiberHttpClient;
import co.paralleluniverse.strands.SuspendableRunnable;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.utils.DateUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.CodingErrorAction;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Batch {

    private Batch() {
    }

    /**
     * 默认2秒批量请求超时
     */
    public static int DEFAULT_TIME_OUT = 2000;
    /**
     * logger
     */
    private static Logger logger;

    /**
     * HttpAsyncClientBuilder
     */
    private static HttpAsyncClientBuilder fiberHttpClientBuilder;

    static {
        try {
            logger = LoggerFactory.getLogger(Batch.class);

            DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig.custom().setConnectTimeout(3600).setIoThreadCount(Runtime.getRuntime().availableProcessors() * 2).setSoTimeout(3600).build());
            PoolingNHttpClientConnectionManager mgr = new PoolingNHttpClientConnectionManager(ioReactor);
            mgr.setDefaultMaxPerRoute(2000);
            mgr.setMaxTotal(5000);
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

            // SSLContext
            SSLContextBuilder sslContextbuilder = new SSLContextBuilder();
            SSLContext sslContext = sslContextbuilder.loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            int timeoutMs = 3600000;// 一小时

            RequestConfig requestConfig = RequestConfig
                    .custom()
                    .setLocalAddress(null)
                    .setSocketTimeout(timeoutMs)
                    .setConnectTimeout(timeoutMs)
                    .setConnectionRequestTimeout(timeoutMs)
                    .build();

            fiberHttpClientBuilder = HttpAsyncClientBuilder
                    .create()
                    .setConnectionManager(mgr)
                    .setDefaultConnectionConfig(connectionConfig)
                    .setSSLContext(sslContext)
                    .setDefaultRequestConfig(requestConfig);


        } catch (Exception e) {
            logger.error("Exception", e);
        }

    }

    /**
     * 批量执行的次数
     */
    static AtomicLong batchCount = new AtomicLong(0);

    /**
     * 失败请求的次数
     */
    static AtomicLong failCount = new AtomicLong(0);

    /**
     * 公用一个client,自动关闭
     */
    static FiberHttpClient client = new FiberHttpClient(fiberHttpClientBuilder.build());

    /**
     * 批量获取
     *
     * @param timeout         超时时间 单位毫秒
     * @param requestPackages RequestPackage数组
     */
    @Suspendable
    public static void exec(final int timeout, final RequestPackage... requestPackages) {
        //校验请求数组是否为空
        if (requestPackages == null || requestPackages.length == 0) {
            return;
        }
        long start = System.currentTimeMillis();
        //建立channel
        final Channel<RequestPackage> objectChannel = Channels.newChannel(requestPackages.length);
        //定义计数器
        final AtomicInteger i = new AtomicInteger(requestPackages.length);
        //定义CountDownLatch
        final CountDownLatch cdl = new CountDownLatch(requestPackages.length);
        try {
            //send message
            new Fiber<Void>(new SuspendableRunnable() {
                @Override
                public void run() throws SuspendExecution, InterruptedException {
                    for (RequestPackage requestPackage : requestPackages) {
                        //设置每一个请求的超时时间都不超过countdownlatch的超时时间
                        requestPackage.setSoTimeout(timeout);
                        //向channel发message
                        objectChannel.send(requestPackage);
                    }
                }
            }).start();

            while (i.getAndDecrement() > 0) { //前面发了多少个消息,这里就要接收多少
                //阻塞等待消息
                final RequestPackage requestPackage = objectChannel.receive();
                //将RequestPackage转为requestBase
                final HttpRequestBase requestBase = HttpClientUtils.createHttpRequestBase(requestPackage);
                //如果是不支持的method,continue
                if (requestBase == null) {
                    cdl.countDown();
                    continue;
                }
                new Fiber<Void>(new SuspendableRunnable() {
                    @Override
                    public void run() throws SuspendExecution, InterruptedException {
                        try {
                            //请求开始时间
                            long startTime = System.currentTimeMillis();
                            //执行请求
                            client.execute(requestBase, new CloseableHttpResponseHandler(requestPackage, cdl));
                            //请求结束时间
                            long endTime = System.currentTimeMillis();
                            //记录性能消耗
                            logger.info("performance timespan = {} ,request {}", endTime - startTime, requestPackage.getUrl());
                        } catch (Exception e) {
                            printRequestError(requestPackage, e);
                        }
                    }
                }).start();
            }
            //计数器的超时时间要比http请求的时间长500ms,以等待所有的请求返回
            boolean await = cdl.await(timeout + 1000, TimeUnit.MILLISECONDS);
            if (!await) {
                //超时结束的
                logger.info("超时结束了{}次", failCount.incrementAndGet());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            objectChannel.close();
        }
        long end = System.currentTimeMillis();
        logger.info("本次请求耗时ms={}", end - start);
    }

    /**
     * 默认2秒超时
     *
     * @param requestPackages request 数组
     */
    @Suspendable
    public static void exec(RequestPackage... requestPackages) {
        if (requestPackages == null || requestPackages.length == 0) {
            return;
        }
        int maxTimeout = DEFAULT_TIME_OUT;
        for (RequestPackage requestPackage : requestPackages) {
            if (requestPackage.getSoTimeout() > 0 && requestPackage.getSoTimeout() > maxTimeout) {
                maxTimeout = requestPackage.getSoTimeout();
            } else if (requestPackage.getRequestConfig() != null && requestPackage.getRequestConfig().getSocketTimeout() > maxTimeout) {
                maxTimeout = requestPackage.getRequestConfig().getSocketTimeout();
            }
        }
        exec(maxTimeout, requestPackages);
    }

    /**
     * 打印具体的错误信息
     *
     * @param requestPackage RequestPackage
     * @param ex             Exception
     */
    private static void printRequestError(RequestPackage requestPackage, Exception ex) {
        StringBuilder builder = new StringBuilder();
        builder.append("\nREQUEST ERROR URL=");
        builder.append(requestPackage.getUrl());
        RequestConfig requestConfig = requestPackage.getRequestConfig();
        if (requestConfig != null) {
            builder.append("\nCONFIG=");
            builder.append(requestConfig);
        }
        logger.error(builder.toString(), ex);
    }
}
