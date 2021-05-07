package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="QueryTestUserResultVO", description="Query submit records")
public class QueryTestUserResultVO {
	
	/** Submit Record Id*/
	@ApiModelProperty(value="Submit Record Id")
	private String testUserId;
	
	/** User answer activity ID*/
	@ApiModelProperty(value="User answer activity ID")
	private String activityId;
	
	/** Class ID*/
	@ApiModelProperty(value="Class ID")
	private String classId;

	/** Group ID*/
	@ApiModelProperty(value = "Group ID")
	private String groupId;

	/** Group Name*/
	@ApiModelProperty(value = "Group Name")
	private String groupName;

	/** User ID*/
	@ApiModelProperty(value="User ID")
	private String userId;

	/** User Name*/
	@ApiModelProperty(value="User Name")
	private String userName;

	/** Test ID*/
	@ApiModelProperty(value="Test ID")
	private String testId;
	
	/** Test Name*/
	@ApiModelProperty(value="Test Name")
	private String testName;

	/** User Score*/
	@ApiModelProperty(value="User Score")
	private Float userScore;

	/** Mark Status*/
	@ApiModelProperty(value="Mark Status")
	private String markStatus;
	
	/** Paper Total Score*/
	@ApiModelProperty(value="Paper Total Score")
	private Float score;

	/** Create Time*/
	@ApiModelProperty(value="Create Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** Start Answer Time */
	@ApiModelProperty(value="Start Answer Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startAnswerTime;

	/** End Answer Time */
	@ApiModelProperty(value="End Answer Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endAnswerTime;

	/** Total answer time */
	@ApiModelProperty(value="Total answer time")
	private Long answerCostTime;
	
	/** Exam ID*/
	@ApiModelProperty(value="Exam ID")
	private String examId;
	
	/** Exam Name*/
	@ApiModelProperty(value="Exam Name")
	private String examName;

	/** Answer List*/
	@ApiModelProperty(value="Answer List")
	private List<QueryUserAnswerInfoVO> answers;

}
