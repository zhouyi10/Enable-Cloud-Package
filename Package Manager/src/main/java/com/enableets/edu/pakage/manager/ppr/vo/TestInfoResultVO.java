package com.enableets.edu.pakage.manager.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * Test Info VO
 * ExamBO
 * @author
 */
@Data
public class TestInfoResultVO implements Serializable{

	/**
	 * testId
	 */	
	private String testId;

	/**
	 * activityId
	 */	
	private String activityId;

	/**
	 * schoolId
	 */	
	private String schoolId;
	
	/**
	 * schoolName
	 */	
	private String schoolName;

	/**
	 * termId
	 */	
	private String termId;
	
	/**
	 * termName
	 */	
	private String termName;

	/**
	 * gradeId
	 */	
	private String gradeId;

	/**
	 * gradeCode
	 */	
	private String gradeCode;

	/**
	 * gradeName
	 */	
	private String gradeName;

	/**
	 * subjectId
	 */	
	private String subjectId;

	/**
	 * subjectCode
	 */	
	private String subjectCode;

	/**
	 * subjectName
	 */	
	private String subjectName;

	/**
	 * testName
	 */	
	private String testName;

	/**
	 * examId
	 */	
	private String examId;

	/**
	 * testPass
	 */	
	private String testPass;

	/**
	 * markType
	 */	
	private String markType;

	/**
	 * testType
	 */	
	private String testType;

	/**
	 * testOrderType
	 */	
	private String testOrderType;

	/**
	 * testPublishType
	 */	
	private String testPublishType;

	/**
	 * testPublishStatus
	 */	
	private String testPublishStatus;

	/**
	 * testPublishTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date testPublishTime;

	/**
	 * testTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date testTime;

	/**
	 * totalTime
	 */	
	private Float totalTime;

	/**
	 * beginTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date beginTime;

	/**
	 * startTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date startTime;

	/**
	 * endTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date endTime;

	/**
	 * startSubmitTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date startSubmitTime;

	/**
	 * endSubmitTime
	 */
	@JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = Constants.DEFAULT_DATE_ZONE)
	@DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
	private java.util.Date endSubmitTime;

	/**
	 * canBlank
	 */	
	private String canBlank;

	/**
	 * rewardScore
	 */	
	private Float rewardScore;

	/**
	 * sender
	 */	
	private String sender;

	/**
	 * senderName
	 */	
	private String senderName;

	/**
	 * creator
	 */	
	private String creator;

	/**
	 * createTime
	 */	
	private java.util.Date createTime;

	/**
	 * updator
	 */	
	private String updator;

	/**
	 * updateTime
	 */	
	private java.util.Date updateTime;

	/**
	 * delStatus
	 */	
	private String delStatus;

	/**
	 * deliveryId
	 */	
	private String deliveryId;

	/**
	 * examName
	 */	
	private String examName;

	/**
	 * testCostTime
	 */	
	private Float testCostTime;

	/**
	 * appId
	 */	
	private String appId;
	
	/** 
	 * Mark Status
	 */
	private String markStatus;
	
	/**
	 * Score
	 */
	private Float score;

	/** Time allowed for late submission*/
	private Integer delaySubmit;
	
	/** Allow repeated submissions*/
	private Integer resubmit;
	
	/** Distribution source*/
	private String from;
	
	/** Send volume to receive object information*/
	private String users;
	
	/** Recipient details*/
	private List<TestRecipientInfoBO> recipients;
	
	/** User ID*/
	private String userId;
	
	/** Whether to hand in paper 0: No 1: Handed in*/
	private String isSubmit;
	
	/** Number of people handed in*/
	private Integer submitCount;
	
	/** total people*/
	private Integer totalCount;
	
	/** Number of people reviewed*/
	private Integer markCount;

}
