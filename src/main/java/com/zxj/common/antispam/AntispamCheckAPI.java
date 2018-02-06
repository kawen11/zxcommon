/*
 * @(#) TextCallbackAPIDemo.java 2016年3月15日
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.zxj.common.antispam;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zxj.common.http.RequestPackage;
import com.zxj.common.http.ResponsePackage;
import com.zxj.common.utils.JsonUtils;

/**
 * 调用易盾反垃圾云服务在线检测接口API
 * 
 * @author dengliming
 * @version 2017年7月4日
 */
public class AntispamCheckAPI {

	/**
	 * logger
	 */
	private static final Logger logger = LoggerFactory.getLogger(AntispamCheckAPI.class);

	private AntispamCheckAPI() {

	}

	/**
	 * 易盾文本类在线检测
	 * 
	 * @param antispamParams 
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public static AntispamResult textCheckAPI(AntispamParams antispamParams) throws UnsupportedEncodingException {
		Map<String, String> params = antispamParams.getParams();
		logger.info("params:{}", params);
		
		// 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
		ResponsePackage response = RequestPackage.post(antispamParams.getApiURL(), params).setCharset("UTF-8").getResponse();
		if (null == response || !response.isSuccess()) {
			throw new RuntimeException("调用易盾文本检测接口失败！");
		}
		String content = response.getContent();
		if (StringUtils.isBlank(content)) {
			throw new RuntimeException("易盾文本检测接口返回值为空！");
		}
		logger.info("content:{}", content);
		
		AntispamResponse infos = new JsonUtils().deserialize(content, new TypeReference<AntispamResponse>() {});
		if (null == infos) {
			throw new RuntimeException("解析易盾文本检测接口返回值失败！ 返回值为：" + content);
		}
		
		if (infos.getCode() == 200) {
			return infos.getResult();
		} else {
			throw new RuntimeException(infos.getCode() + "  " + infos.getMsg());
		}
		
	}
}
