package com.enableets.edu.pakage.manager.core;

/**
 * ajax返回结果包装类
 * @author duffy_ding
 * @since 2017/9/1
 */
public class AjaxRequestResponse {
	
	/** 失败  */
	public static final Integer STATUS_ERROR = 0;
	
	/** 成功  */
	public static final Integer STATUS_SUCCESS = 1;
	
	/** 状态 0失败  1成功  */
	private Integer status;
	
	/** 消息  */
	private String message;
	
	/** 数据  */
	private Object data;
	
	public AjaxRequestResponse(){
		this.status = STATUS_SUCCESS;
	}
	
	public AjaxRequestResponse(Object data){
		this();
		this.data = data;
	}
	
	public AjaxRequestResponse(Integer status, Object data, String message){
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
