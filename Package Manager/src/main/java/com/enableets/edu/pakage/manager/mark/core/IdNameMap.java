package com.enableets.edu.pakage.manager.mark.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 公用map对象(只含有id和name字段)
 * @author walle_yu@enable-ets.com
 * @since 2018年3月13日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdNameMap {
	
	/** 标识  */
	private String id;
	
	/** 名称  */
	private String name;
	
	public IdNameMap() {
		
	}

	public IdNameMap(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
