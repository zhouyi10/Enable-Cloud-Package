package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.ppr.bo.QuestionKnowledgeInfoBO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * Paper Info
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "QueryPaperInfoResultVO", description = "Query Paper Result")
public class QueryPaperInfoResultVO implements java.io.Serializable{

	/** Paper ID */
	@ApiModelProperty(value = "Paper Primary Key")
	private Long paperId;

	/** Paper Name */
	@ApiModelProperty(value = "Paper Name")
	private String name;

	/** Stage Info */
	@ApiModelProperty(value = "Stage Info")
	private CodeNameMapVO stage;

	/** Grade Info */
	@ApiModelProperty(value = "Grade Info")
	private CodeNameMapVO grade;

	/** Subject Info */
	@ApiModelProperty(value = "Subject Info")
	private CodeNameMapVO subject;

	/** Paper Score */
	@ApiModelProperty(value = "Total score")
	private Float totalPoints;
	
	/** Recommended answer time  */
	@ApiModelProperty(value = "Total Answer Time")
	private Long answerCostTime;

	/** Textbook version  */
	@ApiModelProperty(value = "TextBook Version")
	private IdNameMapVO materialVersion;

	/** Test paper node information */
	@ApiModelProperty(value = "")
	private List<PaperNodeInfoVO> nodes;

	/** School Info */
	@ApiModelProperty(value = "School Info")
	private IdNameMapVO school;

	/** User Info */
	@ApiModelProperty(value = "User Info")
	private IdNameMapVO user;

	/** Creator */
	@ApiModelProperty(value = "Creator")
	private String creator;

	/** Create Time */
	@ApiModelProperty(value = "Create Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

	/** Updater */
	@ApiModelProperty(value = "Updator")
	private String updator;

	/** Update Time */
	@ApiModelProperty(value = "Update Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;

	/** Resource ID */
	@ApiModelProperty(value = "Paper Resource ID")
	private Long contentId;

	/** Paper Type*/
	@ApiModelProperty(value = "Paper Type")
	private String paperType;

	/** */
	@ApiModelProperty(value = "Test paper use code")
	private String usageCode;

	/***/
	@ApiModelProperty(value = "Provider Code")
	private String providerCode;

	/***/
	@ApiModelProperty(value = "Knowledge Info")
	private List<QuestionKnowledgeInfoBO> knowledges;
}
