package com.enableets.edu.pakage.manager.mark.bo;

import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;
import com.enableets.edu.pakage.manager.mark.core.IdNameMap;
import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;
import com.enableets.edu.pakage.manager.mark.core.IdNameMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * 试卷详细信息对象dto
 * @author duffy_ding
 * @since 2018/03/14
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaperInfoBO {

	/** 试卷标识  */
	private String paperId;
	
	/** 3.0资源标识*/
	private Long contentId;

	/** 试卷名称 */
	private String name;

	/** 学段标识  */
	private CodeNameMap stage;
	
	/** 年级标识  */
	private CodeNameMap grade;
	
	/** 学科标识  */
	private CodeNameMap subject;

	/** 试卷总分 */
	private Float totalPoints;

	/** 标签信息  */
	private List<CodeNameMap> tags;

	/** 试卷节点信息  */
	private List<PaperNodeInfoBO> nodes;
	
	/** 派卷人员信息  */
	private IdNameMap user;
	
	/** 学校信息  */
	private IdNameMap school;
	
	/** 学生名称(拼装学生答案时使用) */
	private String userName;

	private String userId;
	
	/** 学生分数(拼装学生答案时使用) */
	private Float userScore;

	/* 派卷学生班级信息*/
	private List<TestRecipientGroupBO> sendPaperGroups;

	public List<TestRecipientGroupBO> getSendPaperGroups() {
		return sendPaperGroups;
	}

	public void setSendPaperGroups(List<TestRecipientGroupBO> sendPaperGroups) {
		this.sendPaperGroups = sendPaperGroups;
	}

	public String getPaperId() {
		return paperId;
	}

	public void setPaperId(String paperId) {
		this.paperId = paperId;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CodeNameMap getStage() {
		return stage;
	}

	public void setStage(CodeNameMap stage) {
		this.stage = stage;
	}

	public CodeNameMap getGrade() {
		return grade;
	}

	public void setGrade(CodeNameMap grade) {
		this.grade = grade;
	}

	public CodeNameMap getSubject() {
		return subject;
	}

	public void setSubject(CodeNameMap subject) {
		this.subject = subject;
	}

	public Float getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Float totalPoints) {
		this.totalPoints = totalPoints;
	}

	public List<CodeNameMap> getTags() {
		return tags;
	}

	public void setTags(List<CodeNameMap> tags) {
		this.tags = tags;
	}

	public List<PaperNodeInfoBO> getNodes() {
		return nodes;
	}

	public void setNodes(List<PaperNodeInfoBO> nodes) {
		this.nodes = nodes;
	}

	public IdNameMap getUser() {
		return user;
	}

	public void setUser(IdNameMap user) {
		this.user = user;
	}

	public IdNameMap getSchool() {
		return school;
	}

	public void setSchool(IdNameMap school) {
		this.school = school;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Float getUserScore() {
		return userScore;
	}

	public void setUserScore(Float userScore) {
		this.userScore = userScore;
	}
}
