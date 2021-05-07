package com.enableets.edu.pakage.framework.ppr.bo;

import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Paper Info
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperInfoBO implements java.io.Serializable{

	/** Paper ID */
	private Long paperId;

	/** Paper Name */
	private String name;

	/** Stage Info */
	private CodeNameMapBO stage;

	/** Grade Info */
	private CodeNameMapBO grade;

	/** Subject Info */
	private CodeNameMapBO subject;

	/** Paper Score */
	private Float totalPoints;
	
	/** Recommended answer time  */
	private Long answerCostTime;

	/** Textbook version  */
	private IdNameMapBO materialVersion;

	/** Test paper node information */
	private List<PaperNodeInfoBO> nodes;

	/** School Info */
	private IdNameMapBO school;

	/** User Info */
	private IdNameMapBO user;

	/** Creator */
	private String creator;

	/** Create Time */
	private java.util.Date createTime;

	/** Updater */
	private String updator;

	/** Update Time */
	private java.util.Date updateTime;

	/** Resource ID */
	private Long contentId;

	/** User ID*/
	private String userId;

	/** */
	private String usageCode;

	/** */
	private CodeNameMapBO zone;

	/***/
	private String providerCode;

	/***/
	private String paperType;

	/***/
	private List<QuestionKnowledgeInfoBO> knowledges;
}
