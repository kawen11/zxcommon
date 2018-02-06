package com.zxj.common.sms;

/**
 * 短信消息
 */
public class SmsMessage<T> {

	/**
	 * 类型（默认4）
	 */
	private String dtype = "4";

	/**
	 * 手机号
	 */
	private T mobile;

	/**
	 * 发送内容
	 */
	private String content;

	/**
	 * 是否加密（默认不加密）
	 */
	private Boolean isencrypt = false;

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public T getMobile() {
		return mobile;
	}

	public void setMobile(T mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsencrypt() {
		return isencrypt;
	}

	public void setIsencrypt(Boolean isencrypt) {
		this.isencrypt = isencrypt;
	}
}
