package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 答案信息对象DTO
 * @author walle_yu@enable-ets.com
 * @since 2018年3月14日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerInfoBO {
	
	/** 答案显示文本  */
	private String lable;
	
	/** 答案匹配策略  */
	private String strategy;
	
	/** 答案解析  */
	private String analysis;

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}
	
}
