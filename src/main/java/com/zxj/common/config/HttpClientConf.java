package com.zxj.common.config;

/**
 * HttpClientConf
 *
 * @author 
 */
public class HttpClientConf {
    /**
     * 总连接数
     */
    private static int poolingHttpClientConnectionManagerMaxTotal = 1000;

    /**
     * 最大路由数
     */
    private static int poolingHttpClientConnectionManagerDefaultMaxPerRoute = 500;

    /**
     * 从连接池中获取到连接的最长时间
     */
    private static int httpClientRequestTimeout = 1 * 1000;

    /**
     * 连接超时时间
     */
    private static int httpClientConnectTimeout = 3 * 1000;

    /**
     * 数据传输超时
     */
    private static int httpClientSocketTimeout = 15 * 1000;


    public static int getPoolingHttpClientConnectionManagerMaxTotal() {
        return poolingHttpClientConnectionManagerMaxTotal;
    }

    public static void setPoolingHttpClientConnectionManagerMaxTotal(int poolingHttpClientConnectionManagerMaxTotal) {
        HttpClientConf.poolingHttpClientConnectionManagerMaxTotal = poolingHttpClientConnectionManagerMaxTotal;
    }

    public static int getPoolingHttpClientConnectionManagerDefaultMaxPerRoute() {
        return poolingHttpClientConnectionManagerDefaultMaxPerRoute;
    }

    public static void setPoolingHttpClientConnectionManagerDefaultMaxPerRoute(int poolingHttpClientConnectionManagerDefaultMaxPerRoute) {
        HttpClientConf.poolingHttpClientConnectionManagerDefaultMaxPerRoute = poolingHttpClientConnectionManagerDefaultMaxPerRoute;
    }

    public static int getHttpClientRequestTimeout() {
        return httpClientRequestTimeout;
    }

    public static void setHttpClientRequestTimeout(int httpClientRequestTimeout) {
        HttpClientConf.httpClientRequestTimeout = httpClientRequestTimeout;
    }

    public static int getHttpClientConnectTimeout() {
        return httpClientConnectTimeout;
    }

    public static void setHttpClientConnectTimeout(int httpClientConnectTimeout) {
        HttpClientConf.httpClientConnectTimeout = httpClientConnectTimeout;
    }

    public static int getHttpClientSocketTimeout() {
        return httpClientSocketTimeout;
    }

    public static void setHttpClientSocketTimeout(int httpClientSocketTimeout) {
        HttpClientConf.httpClientSocketTimeout = httpClientSocketTimeout;
    }
}
