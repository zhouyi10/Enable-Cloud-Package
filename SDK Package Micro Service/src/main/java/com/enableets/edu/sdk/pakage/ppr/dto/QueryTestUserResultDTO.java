package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query submit records
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryTestUserResultDTO {
	
	/** Submit Record Id*/
	private String testUserId;
	
	/** User answer activity ID*/
	private String activityId;
	
	/** Class ID*/
	private String classId;

	/** Group ID*/
	private String groupId;

	/** Group Name*/
	private String groupName;

	/** User ID*/
	private String userId;

	/** User Name*/
	private String userName;

	/** Test ID*/
	private String testId;
	
	/** Test Name*/
	private String testName;

	/** User Score*/
	private Float userScore;

	/** Mark Status*/
	private String markStatus;
	
	/** Paper Total Score*/
	private Float score;

	/** Create Time*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** Start Answer Time */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startAnswerTime;

	/** End Answer Time */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endAnswerTime;

	/** Total answer time */
	private Long answerCostTime;
	
	/** Exam ID*/
	private String examId;
	
	/** Exam Name*/
	private String examName;

	/** Answer List*/
	private List<QueryUserAnswerInfoDTO> answers;

}
