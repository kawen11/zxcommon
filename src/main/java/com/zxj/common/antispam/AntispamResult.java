package com.zxj.common.antispam;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 易盾反垃圾云服务接口公共参数
 * 
 * @author dengliming
 * @version 2017年7月4日
 */
public class AntispamResult {

	/** 检测结果，0：通过，1：嫌疑，2：不通过 */
	private Integer action;
	/** 本次请求数据标识，可以根据该标识查询数据最新结果 */
	private String taskid;
	/** 分类信息 */
	private List<JsonNode> labels;
	/** 业务ID，易盾根据产品业务特点分配 */
	private String businessid;
	
	/**
	 * 检测结果，0：通过，1：嫌疑，2：不通过
	 * @return
	 */
	public Integer getAction() {
		return action;
	}

	/**
	 * 检测结果，0：通过，1：嫌疑，2：不通过
	 * @param action
	 */
	public void setAction(Integer action) {
		this.action = action;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public List<JsonNode> getLabels() {
		return labels;
	}

	public void setLabels(List<JsonNode> labels) {
		this.labels = labels;
	}

	public String getBusinessid() {
		return businessid;
	}

	public void setBusinessid(String businessid) {
		this.businessid = businessid;
	}

	@Override
	public String toString() {
		return "AntispamResult [action=" + action + ", taskid=" + taskid + ", labels=" + labels + ", businessid=" + businessid + "]";
	}

}
