package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 试卷结构节点信息dto
 * @author duffy_ding
 * @since 2018/01/14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperNodeInfoBO {
	
	/** 试卷节点标识 */
	private String nodeId;
	
	/** 父节点标识 */
	private String parentId;
	
	/** 节点名称 */
	private String name;
	
	/** 节点顺序 */
	private Integer sequencing;

	/** 内部编号 */
	private Integer internalNo;

	/** 外部编号，如1.1 */
	private String externalNo;
	
	/** 节点描述 */
	private String description;
	
	/** 节点层级 */
	private Integer level;
	
	/** 节点分数 */
	private Float points;
	
	/** 试题信息 */
	private PaperQuestionInfoBO question;

	/** 子节点列表(改字段存储子题目节点，便于页面显示题目)*/
	private List<PaperNodeInfoBO> children;
	
	/** 子题目数量(存储题目下子题目个数) */
	private Integer childAmount;
	
	/** 用户答案信息 */
	private UserAnswerBO userAnswer;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSequencing() {
		return sequencing;
	}

	public void setSequencing(Integer sequencing) {
		this.sequencing = sequencing;
	}

	public Integer getInternalNo() {
		return internalNo;
	}

	public void setInternalNo(Integer internalNo) {
		this.internalNo = internalNo;
	}

	public String getExternalNo() {
		return externalNo;
	}

	public void setExternalNo(String externalNo) {
		this.externalNo = externalNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Float getPoints() {
		return points;
	}

	public void setPoints(Float points) {
		this.points = points;
	}

	public PaperQuestionInfoBO getQuestion() {
		return question;
	}

	public void setQuestion(PaperQuestionInfoBO question) {
		this.question = question;
	}

	public List<PaperNodeInfoBO> getChildren() {
		return children;
	}

	public void setChildren(List<PaperNodeInfoBO> children) {
		this.children = children;
	}

	public Integer getChildAmount() {
		return childAmount;
	}

	public void setChildAmount(Integer childAmount) {
		this.childAmount = childAmount;
	}

	public UserAnswerBO getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(UserAnswerBO userAnswer) {
		this.userAnswer = userAnswer;
	}
}
