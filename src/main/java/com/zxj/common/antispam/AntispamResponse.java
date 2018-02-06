package com.zxj.common.antispam;

/**
 * 易盾反垃圾云服务接口公共参数
 * 
 * @author dengliming
 * @version 2017年7月4日
 */
public class AntispamResponse {

	/** 返回码 */
	private Integer code;
	/** 返回码描述 */
	private String msg;
	/** 响应结果 */
	private AntispamResult result;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public AntispamResult getResult() {
		return result;
	}

	public void setResult(AntispamResult result) {
		this.result = result;
	}

}
