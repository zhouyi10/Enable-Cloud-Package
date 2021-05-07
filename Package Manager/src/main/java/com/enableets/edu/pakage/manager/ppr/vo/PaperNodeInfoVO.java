package com.enableets.edu.pakage.manager.ppr.vo;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Test paper structure
 */
@Data
public class PaperNodeInfoVO implements Comparable<PaperNodeInfoVO> {

	/** Node ID */
	private String nodeId;

	/** Parent Node ID */
	private String parentNodeId;

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
	private PaperQuestionVO question;

	/** Sub-node list (change the field to store the sub-topic node, which is convenient for the page to display the topic)*/
	private List<PaperNodeInfoVO> children;

	/** Number of children (the number of sub-topics under the storage topic) */
	private Integer childAmount;

	private String creator;

	private Date createTime;

	private String updator;

	private Date updateTime;

	@Override
	public int compareTo(PaperNodeInfoVO arg0) {
		return this.getSequencing().compareTo(arg0.getSequencing());
	}
}
