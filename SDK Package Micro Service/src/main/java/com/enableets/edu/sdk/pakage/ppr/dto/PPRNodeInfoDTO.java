package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Test paper structure
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PPRNodeInfoDTO{

	/** Node ID */
	private Long nodeId;

	/** Parent Node ID */
	private Long parentNodeId;

	/** */
	private String name;

	/**  */
	private String description;

	/**  */
	private Integer sequencing;

	/** Node level */
	private Integer level;

	/** Internal number */
	private Integer internalNo;

	/** External number to Explain，Such as 1.1、I-I */
	private String externalNo;

	/** Score */
	private Float points;

	/** Question info  */
	private PPRQuestionDTO question;
}
