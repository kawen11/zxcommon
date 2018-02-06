package com.zxj.common.sms;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.http.RequestPackage;
import com.zxj.common.http.ResponsePackage;

/**
 * 执行发送短信线程
 */
class CallSms implements Runnable {

    /**
     * logger
     */
    private static Logger logger = LoggerFactory.getLogger(CallSms.class);

    /**
     * 短信接口地址
     */
    private String url;

    /**
     * 短信内容
     */
    private Map<String, Object> params = new HashMap<>();

    public CallSms() {

    }
    
    public CallSms(String url, Map<String, Object> params) {
        this.url = url;
        this.params.putAll(params);
    }
    
    public void run() {
        ResponsePackage response = RequestPackage.post(url, params).getResponse();
        if (response != null && response.isSuccess() && "1".equals(response.getContent())) {
        	logger.info("发送短信成功：【{}】", params);
	    } else {
	    	logger.error("发送短信失败：【{}】", params);
	    }
    }

}
