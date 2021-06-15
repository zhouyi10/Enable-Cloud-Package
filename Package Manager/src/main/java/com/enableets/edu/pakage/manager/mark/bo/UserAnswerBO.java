package com.enableets.edu.pakage.manager.mark.bo;

import java.util.List;

/**
 *  逐题批阅页面一条批阅记录答案信息
 *	@author walle_yu@enable-ets.com
 *  @2017/6/7
 */
public class UserAnswerBO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 学生id
	 */
	private String userId;
	
	/**
	 * 学生姓名
	 */
	private String userName;
	
	/**
	 * 答题id
	 */
	private String answerId;
	
	/**
	 * 学生答案
	 */
	private String userAnswer;
	
	/**
	 * 试卷得分
	 */
	private String userScore;
	
	/**
	 * 答题得分
	 */
	private String answerScore;
	
	/**
	 * 是否回答正确
	 */
	private String answerStatus;
	
	/**
	 * 批阅状态
	 */
	private String markStatus;
	
	/**
	 * 学生交卷id
	 */
	private String testUserId;
	
	/**
	 * 题目标识
	 */
	private String questionId;
	
	/**
	 * 题目分数
	 */
	private Float questionScore;
	
	private String abilityId;
	
	private String abilityName;
	
	/**
	 * 绘图详情
	 */
	private List<CanvasInfoBO> canvases;

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

	public String getAnswerId() {
		return answerId;
	}

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public String getUserScore() {
		return userScore;
	}

	public void setUserScore(String userScore) {
		this.userScore = userScore;
	}

	public String getAnswerScore() {
		return answerScore;
	}

	public void setAnswerScore(String answerScore) {
		this.answerScore = answerScore;
	}

	public String getAnswerStatus() {
		return answerStatus;
	}

	public void setAnswerStatus(String answerStatus) {
		this.answerStatus = answerStatus;
	}

	public String getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}

	public String getTestUserId() {
		return testUserId;
	}

	public void setTestUserId(String testUserId) {
		this.testUserId = testUserId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public Float getQuestionScore() {
		return questionScore;
	}

	public void setQuestionScore(Float questionScore) {
		this.questionScore = questionScore;
	}

	public String getAbilityId() {
		return abilityId;
	}

	public void setAbilityId(String abilityId) {
		this.abilityId = abilityId;
	}

	public String getAbilityName() {
		return abilityName;
	}

	public void setAbilityName(String abilityName) {
		this.abilityName = abilityName;
	}

	public List<CanvasInfoBO> getCanvases() {
		return canvases;
	}

	public void setCanvases(List<CanvasInfoBO> canvases) {
		this.canvases = canvases;
	}

	@Override
	public String toString() {
		return "UserAnswerBO{" +
				"userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", answerId='" + answerId + '\'' +
				", userAnswer='" + userAnswer + '\'' +
				", userScore='" + userScore + '\'' +
				", answerScore='" + answerScore + '\'' +
				", answerStatus='" + answerStatus + '\'' +
				", markStatus='" + markStatus + '\'' +
				", testUserId='" + testUserId + '\'' +
				", questionId='" + questionId + '\'' +
				", questionScore=" + questionScore +
				", abilityId='" + abilityId + '\'' +
				", abilityName='" + abilityName + '\'' +
				", canvases=" + canvases +
				'}';
	}
}
