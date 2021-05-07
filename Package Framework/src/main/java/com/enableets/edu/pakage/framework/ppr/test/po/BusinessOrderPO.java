package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Date;
import lombok.Data;

/**
 * business order form po
 * @author duffy_ding
 * @since 2019/08/22
 */
@Entity
@Table(name = "business_order")
@Data
public class BusinessOrderPO {

	@Id
	@Column(name="order_id")
	private String orderId;

	@Column(name="business_id")
	private String businessId;

	@Column(name="type")
	private String type;

	@Column(name="origin_data")
	private String originData;

	@Column(name="extend_attrs")
	private String extendAttrs;

	@Column(name="status")
	private Integer status;

	@Column(name="version")
	private Integer version;

	@Column(name="retry_times")
	private Integer retryTimes;

	@Column(name="error_code")
	private String errorCode;

	@Column(name="error_message")
	private String errorMessage;

	@Column(name = "creator")
	private String creator;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "updator")
	private String updator;

	@Column(name = "update_time")
	private Date updateTime;
}
