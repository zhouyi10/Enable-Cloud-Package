package com.enableets.edu.pakage.core.core.htmlparser.multipart;

import cn.hutool.json.JSONObject;
import java.io.Serializable;

/**
 * Return result entity
 *
 * @author caleb_liu@enable-ets.com
 * @since 2019-01-30 10:50
 */
public class ResultBean<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	// success status
	public static final int SUCCESS = 1;
	// processing status
	public static final int PROCESSING = 0;
	// failure status
	public static final int FAIL = -1;

	// Message
	private String msg = "success";
	// status default success
	private int code = SUCCESS;
	// Remark
	private String remark;
	// return data
	private T data;

	public ResultBean() {
		super();
	}

	public ResultBean(T data) {
		super();
		this.data = data;
	}

	/**
	 * Use exceptions to create results
	 */
	public ResultBean(Throwable e) {
		super();
		this.msg = e.toString();
		this.code = FAIL;
	}

	/**
	 * Default Instance<BR>
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public static <T> ResultBean<T> newInstance() {
		ResultBean<T> instance = new ResultBean<T>();
		instance.code = SUCCESS;
		instance.msg = "success";
		return instance;
	}

	/**
	 * Default Instance<BR>
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public static <T> ResultBean<T> newInstance(T data) {
		ResultBean<T> instance = new ResultBean<T>();
		//默认返回信息
		instance.code = SUCCESS;
		instance.msg = "success";
		instance.data = data;
		return instance;
	}

	/**
	 * @param code
	 * @param msg
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public static <T> ResultBean<T> newInstance(int code, String msg) {
		ResultBean<T> instance = new ResultBean<T>();
		instance.code = code;
		instance.msg = msg;
		return instance;
	}

	/**
	 * @param code
	 * @param msg
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public static <T> ResultBean<T> newInstance(int code, String msg, T data) {
		ResultBean<T> instance = new ResultBean<T>();
		instance.code = code;
		instance.msg = msg;
		instance.data = data;
		return instance;
	}

	/**
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> setData(T data) {
		this.data = data;
		return this;
	}

	/**
	 * @param msg
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	/**
	 * @param code
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> setCode(int code) {
		this.code = code;
		return this;
	}

	/**
	 * @param remark
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	/**
	 * @param msg
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> success(String msg, T data) {
		this.code = SUCCESS;
		this.data = data;
		this.msg = msg;
		return this;
	}

	/**
	 * @param msg
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> success(String msg) {
		this.code = SUCCESS;
		this.msg = msg;
		return this;
	}

	/**
	 * @param msg
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> processing(String msg, T data) {
		this.code = PROCESSING;
		this.data = data;
		this.msg = msg;
		return this;
	}

	/**
	 * @param msg
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> processing(String msg) {
		this.code = PROCESSING;
		this.msg = msg;
		return this;
	}

	/**
	 * @param msg
	 * @param data
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> fail(String msg, T data) {
		this.code = FAIL;
		this.data = data;
		this.msg = msg;
		return this;
	}

	/**
	 * @param msg
	 * @return ResultBean<T><BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public ResultBean<T> fail(String msg) {
		this.code = FAIL;
		this.msg = msg;
		return this;
	}

	public T getData() {
		return data;
	}

	public String getMsg() {
		return msg;
	}

	public int getCode() {
		return code;
	}

	public String getRemark() {
		return remark;
	}

	/**
	 * @return String<BR>
	 * @throws <BR>
	 * @since 2.0
	 */
	public String json() {
		return new JSONObject(this).toString();
	}
}
