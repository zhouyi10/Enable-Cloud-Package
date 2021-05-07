package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import lombok.Data;

/**
 * Business Order BO
 * @author duffy_ding
 * @since 2019/08/22
 */
@Data
public class BusinessOrderBO {

	/** Order primary key */
	private String orderId;

	/** business identity */
	private String businessId;

	/** type such as： _ANSWER、_PUSH_CONTENT */
	private String type;

	/** raw data */
	private String originData;

	/** Extra data, parameters saved when starting processing, for retry */
	private String extendAttrs;

	/** State default 0 (-1 processing failed 0 pending processing 1 processing successful 2 processing)*/
	private Integer status;

	/** Version Default 0 Message in the queue 1 Message processing */
	private Integer version;

	/** number of retries */
	private Integer retryTimes = 0;

	/** error code */
	private String errorCode;

	/** exception message */
	private String errorMessage;

	/** creator */
	private String creator;

	/** creation time */
	private Date createTime;

	/** updater */
	private String updator;

	/** update time */
	private Date updateTime;
}
