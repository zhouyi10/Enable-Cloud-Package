package com.enableets.edu.pakage.manager.mark.core;

import java.io.Serializable;

/**
 * 公用map对象(只含有code和name字段)
 * @author walle_yu@enable-ets.com
 * @since 2018年3月15日
 */
public class CodeNameMap implements Serializable {

	/** serialVersionUID  */
	private static final long serialVersionUID = -4553584480118331902L;

	/** 编码  */
	private String code;
	
	/** 名称  */
	private String name;
	
	public CodeNameMap() {
		
	}

	public CodeNameMap(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
