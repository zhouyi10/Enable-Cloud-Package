package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Paper Info
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryPaperInfoResultDTO implements java.io.Serializable{

	/** Paper ID */
	private Long paperId;

	/** Paper Name */
	private String name;

	/** Stage Info */
	private CodeNameMapDTO stage;

	/** Grade Info */
	private CodeNameMapDTO grade;

	/** Subject Info */
	private CodeNameMapDTO subject;

	/** Paper Score */
	private Float totalPoints;
	
	/** Recommended answer time  */
	private Long answerCostTime;

	/** Textbook version  */
	private IdNameMapDTO materialVersion;

	/** Test paper node information */
	private List<PaperNodeInfoDTO> nodes;

	/** School Info */
	private IdNameMapDTO school;

	/** User Info */
	private IdNameMapDTO user;

	/** Creator */
	private String creator;

	/** Create Time */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createTime;

	/** Updater */
	private String updator;

	/** Update Time */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateTime;

	/** Resource ID */
	private Long contentId;

	/** */
	private String usageCode;

	/** */
	private CodeNameMapDTO zone;

	/***/
	private String providerCode;

	/***/
	private String paperType;

	/***/
	private List<QuestionKnowledgeInfoDTO> knowledges;
}
