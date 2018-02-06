package com.zxj.common.sms;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zxj.common.utils.BeanUtils;

/**
 * 短信工具类
 */
public class SmsUtils {
	
	/**
	 * logger
	 */
    private static final Logger logger = LoggerFactory.getLogger(SmsUtils.class);
    
    /**
	 * 短信接口地址
	 */
	private static final String URL = "";
	
	/**
	 * 发送单条短信
	 * 
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public static void sendSms(SmsMessage<String> message) {
		try {
			if (null != message) {
				// 把SmsMessage转成Map
				Map<String, Object> params = BeanUtils.getBeanMap(message);
				new Thread(new CallSms(URL, params)).start();
			}
		} catch (Exception e) {
			logger.error("发送单条短信失败：", e);
		}
	}
	
	/**
	 * 发送群组短信
	 * 
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	public static void sendGroupSms(SmsMessage<List<String>> message) {
		try {
			if (null != message) {
				// 把SmsMessage转成Map
				Map<String, Object> params = BeanUtils.getBeanMap(message);
				for (String mobile : message.getMobile()) {
					params.put("mobile", mobile);
					new Thread(new CallSms(URL, params)).start();
				}
			}
		} catch (Exception e) {
			logger.error("发送群组短信失败：", e);
		}
	}
	
	public static void main(String[] args) {
		SmsMessage<String> message = new SmsMessage<>();
		message.setMobile("15001187708");
		message.setContent("测试短信，不合理吧");
		SmsUtils.sendSms(message);
		
		SmsMessage<List<String>> message1 = new SmsMessage<>();
		message1.setMobile(Arrays.asList("15101069615,15001187708".split(",")));
		message1.setContent("测试短信，不合理吧");
		SmsUtils.sendGroupSms(message1);
	}
}
