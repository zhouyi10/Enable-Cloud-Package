package com.enableets.edu.pakage.manager.mark.core;

/**
 * ajax返回结果包装类
 * @author duffy_ding
 * @since 2017/11/10
 */
public class OperationResult<T> {
	
	/** 失败  */
	public static final Integer STATUS_ERROR = 0;
	
	/** 成功  */
	public static final Integer STATUS_SUCCESS = 1;
	
	/** 状态 0失败  1成功  */
	private Integer status;
	
	/** 消息  */
	private String message;
	
	/** 数据  */
	private T data;
	
	public OperationResult(){
		this.status = STATUS_SUCCESS;
	}
	
	public OperationResult(T data){
		this();
		this.data = data;
	}
	
	public OperationResult(Integer status, T data, String message){
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public static OperationResult success(Object data) {
		return new OperationResult(data);
	}

	public static OperationResult error(Object data, String message) {
		return new OperationResult(STATUS_ERROR, data, message);
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
