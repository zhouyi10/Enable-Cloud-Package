package com.enableets.edu.pakage.manager.ppr.bo;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Test paper structure
 */
@Data
public class PaperNodeInfoBO implements Comparable<PaperNodeInfoBO> {

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

	/** Score */
	private String realPoints;

	/** Question info  */
	private PaperQuestionBO question;

	/** Sub-node list (change the field to store the sub-topic node, which is convenient for the page to display the topic)*/
	private List<PaperNodeInfoBO> children;

	/** Number of children (the number of sub-topics under the storage topic) */
	private Integer childAmount;

	private String creator;

	private Date createTime;

	private String updator;

	private Date updateTime;

	@Override
	public int compareTo(PaperNodeInfoBO arg0) {
		return this.getSequencing().compareTo(arg0.getSequencing());
	}
}
