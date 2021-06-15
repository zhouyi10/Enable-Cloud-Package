package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 题目选项信息DTO
 * @author walle_yu@enable-ets.com
 * @since 2018年3月14日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionOptionInfoBO {
	
	/** 选项标题  */
	private String alias;
	
	/** 选项内容  */
	private String lable;
	
	/** 选项排序  */
	private String sequencing;

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public String getSequencing() {
		return sequencing;
	}

	public void setSequencing(String sequencing) {
		this.sequencing = sequencing;
	}
}
