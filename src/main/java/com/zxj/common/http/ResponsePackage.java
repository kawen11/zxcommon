package com.zxj.common.http;

import org.apache.http.HttpStatus;

import java.util.Map;

/**
 * ResponsePackage
 *
 * @author 
 * @date 2016/3/8
 */
public class ResponsePackage {
    /**
     * http status
     */
    int httpStatus;
    /**
     * content
     */
    String content;
    /**
     * reponse header
     */
    Map<String, String> headers;
    /**
     * reponse cookies
     */
    Map<String, String> cookies;

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return httpStatus == HttpStatus.SC_OK;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }
}
