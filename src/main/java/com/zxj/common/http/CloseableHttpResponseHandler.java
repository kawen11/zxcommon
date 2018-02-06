package com.zxj.common.http;

import co.paralleluniverse.fibers.Suspendable;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.utils.DateUtils;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Suspendable
public class CloseableHttpResponseHandler implements ResponseHandler<CloseableHttpResponse> {

    /**
     * logger
     */
    static Logger logger = LoggerFactory.getLogger(CloseableHttpResponseHandler.class);

    RequestPackage requestPackage;

    CountDownLatch cdl;

    public CloseableHttpResponseHandler(RequestPackage requestPackage, CountDownLatch cdl) {
        this.requestPackage = requestPackage;
        this.cdl = cdl;
    }

    @Suspendable
    @Override
    public CloseableHttpResponse handleResponse(HttpResponse response) {
        try {
            if (response == null) {
                return null;
            }
            ResponsePackage responsePackage = HttpClientUtils.getResponsePackage(response, requestPackage);
            if (responsePackage == null) {
                return null;
            }
            requestPackage.setResponsePackage(responsePackage);
            logger.info("\n{}|REQUEST[{}]---HttpStatus-{}", DateUtils.now(), requestPackage.getUrl(), responsePackage.getHttpStatus());
            if (response instanceof CloseableHttpResponse) {
                CloseableHttpResponse closeableHttpResponse = (CloseableHttpResponse) response;
                closeableHttpResponse.close();
                return closeableHttpResponse;
            }
            return null;
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            cdl.countDown();
        }
        return null;
    }

}
