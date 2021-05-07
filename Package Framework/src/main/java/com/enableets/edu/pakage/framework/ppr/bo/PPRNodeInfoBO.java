package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * Test paper structure
 */
@Data
public class PPRNodeInfoBO {

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
	private PPRQuestionBO question;
}
