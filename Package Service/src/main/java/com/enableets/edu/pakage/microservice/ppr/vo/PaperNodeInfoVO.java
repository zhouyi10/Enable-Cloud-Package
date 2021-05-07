package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Test paper structure
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "paperNodeInfoVO", description = "Test paper node information, flat presentation")
public class PaperNodeInfoVO {

	/** Node ID */
	@ApiModelProperty(value = "Node ID", required = false)
	private Long nodeId;

	/** Parent Node ID */
	@ApiModelProperty(value = "Parent Node ID", required = false)
	private Long parentNodeId;

	/** */
	@ApiModelProperty(value = "Name", required = false)
	private String name;

	/**  */
	@ApiModelProperty(value = "Description", required = false)
	private String description;

	/**  */
	@ApiModelProperty(value = "Node Sequence", required = false)
	private Integer sequencing;

	/** Node level */
	@ApiModelProperty(value = "Node Level", required = false)
	private Integer level;

	/** Internal number */
	@ApiModelProperty(value = "Internal No.", required = false)
	private Integer internalNo;

	/** External number to Explain，Such as 1.1、I-I */
	@ApiModelProperty(value = "External No.", required = false)
	private String externalNo;

	/** Score */
	@ApiModelProperty(value = "Node Total Score", required = false)
	private Float points;

	/** Question info  */
	@ApiModelProperty(value = "Question in Node", required = false)
	private PaperQuestionVO question;
}
