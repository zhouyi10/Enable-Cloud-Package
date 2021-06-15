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
 * Query Test Info
 * @author walle_yu@enable-ets.com
 * @since 2020/08/31
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="QueryTAsTestResultVO", description="Query exam returns VO")
@Data
public class QueryTestInfoResultVO {

	/** Test ID*/
	@ApiModelProperty(value="Test ID")
	private String testId;

	/** Test Document File ID*/
	@ApiModelProperty(value="Test Document File ID")
	private String fileId;

	@ApiModelProperty(value = "Step Id")
	private String stepId;

	/** Activity ID*/
	@ApiModelProperty(value="Activity ID")
	private String activityId;

	/** School ID*/
	@ApiModelProperty(value="School ID")
	private String schoolId;

	/** School Name*/
	@ApiModelProperty(value="School Name")
	private String schoolName;

	/** Term ID*/
	@ApiModelProperty(value="Term ID")
	private String termId;

	/** Term Name*/
	@ApiModelProperty(value="Term Name")
	private String termName;

	/** Grade Code*/
	@ApiModelProperty(value="Grade Code")
	private String gradeCode;

	/** Grade Name*/
	@ApiModelProperty(value="Grade Name")
	private String gradeName;

	/** Subject Code*/
	@ApiModelProperty(value="Subject Code")
	private String subjectCode;

	/** Subject Name*/
	@ApiModelProperty(value="Subject Name")
	private String subjectName;

	/** Test Name*/
	@ApiModelProperty(value="Test Name")
	private String testName;

	/** Exam ID*/
	@ApiModelProperty(value="Exam ID")
	private String examId;

	/** Exam Name*/
	@ApiModelProperty(value="Exam Name")
	private String examName;

	/** Exam Type*/
	@ApiModelProperty(value="Exam Type")
	private String examType;

	/** Score*/
	@ApiModelProperty(value="Score")
	private Float score;

	/** Test Begin Time*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value=" Test Begin Time")
	private Date startTime;

	/** Test End Time*/
	@ApiModelProperty(value="Test End Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;

	/** Test Time*/
	@ApiModelProperty(value="Test Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date testTime;

	/** Start Submit Time*/
	@ApiModelProperty(value="Start Submit Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startSubmitTime;

	/** End Submit Time*/
	@ApiModelProperty(value="End Submit Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endSubmitTime;

	/** Send Paper User ID*/
	@ApiModelProperty(value="Send Paper User ID")
	private String sender;

	/** Send Paper User Name*/
	@ApiModelProperty(value="Send Paper User Name")
	private String senderName;

	/** Test Cost Time*/
	@ApiModelProperty(value="Test Cost Time")
	private Float testCostTime;

	/** Time allowed for late submission*/
	@ApiModelProperty(value="Time allowed for late submission")
	private Integer delaySubmit;

	/** Maximum number of submissions*/
	@ApiModelProperty(value="Maximum number of submissions")
	private Integer resubmit;

	/** Creator*/
	@ApiModelProperty(value="Creator")
	private String creator;

	/** Create Time*/
	@ApiModelProperty(value="Create Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** Updater*/
	@ApiModelProperty(value="Updater")
	private String updator;

	/** Update Time*/
	@ApiModelProperty(value="Update Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;

	/** Exam Recipient details*/
	@ApiModelProperty(value="Exam Recipient details")
	private List<TestRecipientInfoVO> recipients;

	@ApiModelProperty(value = "Activiti Process Instance Id")
	private String processInstantId;

}
