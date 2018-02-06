package com.zxj.common.antispam;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.zxj.common.utils.StringUtils;

/**
 * 易盾反垃圾云服务接口公共参数
 * 
 * @author dengliming
 * @version 2017年7月4日
 */
public class AntispamParams {

	/** 易盾接口url */
	private String apiURL;

	/** 产品密钥ID，产品标识 */
	private String secretId;

	/** 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露 */
	private String secretKey;

	/** 业务ID，易盾根据产品业务特点分配 */
	private String businessId;

	/** 版本号 默认v3 */
	private String version;

	/** 唯一标识dateId */
	private String dataId;

	/** 检测文本内容 */
	private String content;

	/** images为json数组，支持批量检测 32张或10MB */
	private String images;

	/** 直播流地址 */
	private String url;

	/** 子数据类型，与易盾反垃圾云服务约定即可 */
	private Integer dataType;
	
	/** 用户IP地址 */
	private String ip;

	/** 用户唯一标识，如果无需登录则为空 */
	private String account;

	/**
	 * 用户设备类型， 1：web， 2：wap， 3：android， 4：iphone， 5：ipad， 6：pc， 7：wp
	 */
	private Integer deviceType;

	/**
	 * 用户设备 id
	 */
	private Integer deviceId;

	/**
	 * 数据回调参数，调用方根据业务情况自行设计，当调用文本离线结果获取接口时，该接口会原样返回该字段，详细见文本离线检测结果获取。作为数据处理标识，因此该字段应该设计为能唯一定位到该次请求的数据结构，如对用户的昵称进行检测，dataId可设为用户标识（用户ID），用户修改多次，每次请求数据的dataId可能一致，但是callback参数可以设计成定位该次请求的数据结构，比如callback字段设计成json，包含dataId和请求的时间戳等信息，当然如果不想做区分，也可以直接把callback设置成dataId的值。
	 */
	private String callback;

	/**
	 * 发表时间，UNIX 时间戳(毫秒值)
	 */
	private Long publishTime;
	
	/**
	 * 易盾接口url
	 * 
	 * @return
	 */
	public String getApiURL() {
		return apiURL;
	}

	/**
	 * 易盾接口url
	 * 
	 * @param apiURL
	 */
	public void setApiURL(String apiURL) {
		this.apiURL = apiURL;
	}

	/**
	 * 
	 * 产品密钥ID，产品标识
	 * 
	 * @return
	 */
	public String getSecretId() {
		return secretId;
	}

	/**
	 * 
	 * 产品密钥ID，产品标识
	 * 
	 * @param secretid
	 */
	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	/**
	 * 
	 * 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
	 * 
	 * @return
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * 
	 * 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
	 * 
	 * @param secretKey
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * 
	 * 业务ID，易盾根据产品业务特点分配
	 * 
	 * @return
	 */
	public String getBusinessId() {
		return businessId;
	}

	/**
	 * 
	 * 业务ID，易盾根据产品业务特点分配
	 * 
	 * @param businessId
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * 版本号 可选值v3 
	 * @return
	 */
	public String getVersion() {
		return this.version;
	}

	/**
	 * 版本号 可选值v3 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 唯一标识dateId
	 * 
	 * @return
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * 
	 * 唯一标识dateId
	 * 
	 * @param dataId
	 */
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	/**
	 * 
	 * 用户发表内容;请注意为了检测效果和性能，如果该字段长度超过1万字符，会截取前面1万字符进行检测和存储
	 * 
	 * @return
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 
	 * images为json数组，支持批量检测 32张或10MB
	 * 
	 * @return
	 */
	public String getImages() {
		return images;
	}

	/**
	 * 
	 * images为json数组，支持批量检测 32张或10MB
	 * 
	 * @param images
	 */
	public void setImages(String images) {
		this.images = images;
	}

	/**
	 * 直播流地址
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 直播流地址
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}
	
	public Map<String, String> getParams() throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<>();
		if (!StringUtils.isNoneEmpty(this.getApiURL(), this.getSecretId(), this.getBusinessId(), this.getDataId(), this.getContent(), this.getSecretKey(), this.getVersion())) {
			throw new RuntimeException("参数校验未通过缺少必填参数");
		}

		// 1.设置公共参数
		params.put("secretId", this.getSecretId());
		params.put("businessId", this.getBusinessId());
		params.put("version", this.getVersion());
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("nonce", String.valueOf(new Random().nextInt()));

		// 2.设置私有参数
		params.put("dataId", this.getDataId());
		params.put("content", StringUtils.htmlText(this.getContent()));

		
		if(null != this.getDataType()){
			params.put("ip", this.getDataType().toString());
		}
		
		if(StringUtils.isNotBlank(this.getIp())){
			params.put("ip", this.getIp());
		}
		
		if(StringUtils.isNotBlank(this.getAccount())){
			params.put("account", this.getAccount());
		}
		
		if(null != this.getDeviceType()){
			params.put("deviceType", this.getDeviceType().toString());
		}
		
		if(null != this.getDeviceId()){
			params.put("deviceId", this.getDeviceId().toString());
		}
		
		if(StringUtils.isNotBlank(this.getCallback())){
			params.put("callback", this.getCallback());
		}
		
		if(null != this.getPublishTime()){
			params.put("publishTime", this.getPublishTime().toString());
		}
		// 3.生成签名信息
		String signature = SignatureUtils.genSignature(this.getSecretKey(), params);
		params.put("signature", signature);
		return params;
	}

}
