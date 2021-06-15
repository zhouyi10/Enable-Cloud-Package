package com.enableets.edu.pakage.manager.mark.bo;


import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;
import com.enableets.edu.pakage.manager.mark.core.CodeNameMap;

import java.io.Serializable;
import java.util.List;

/**
 * 试题对象
 * ExamBO
 * @author xiaow
 */

public class TestInfoBO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * testId
	 */	
	private String testId;

	/**
	 * fileId
	 */
	private String fileId;

	/**
	 * activityId
	 */	
	private String activityId;

	/**
	 * schoolId
	 */	
	private String schoolId;
	
	/**
	 * schoolName
	 */	
	private String schoolName;

	/**
	 * termId
	 */	
	private String termId;
	
	/**
	 * termName
	 */	
	private String termName;

	/**
	 * gradeId
	 */	
	private String gradeId;

	/**
	 * gradeCode
	 */	
	private String gradeCode;

	/**
	 * gradeName
	 */	
	private String gradeName;

	/**
	 * subjectId
	 */	
	private String subjectId;

	/**
	 * subjectCode
	 */	
	private String subjectCode;

	/**
	 * subjectName
	 */	
	private String subjectName;

	/**
	 * testName
	 */	
	private String testName;

	/**
	 * examId
	 */	
	private String examId;

	/**
	 * testPass
	 */	
	private String testPass;

	/**
	 * markType
	 */	
	private String markType;

	/**
	 * testType
	 */	
	private String testType;

	/**
	 * testOrderType
	 */	
	private String testOrderType;

	/**
	 * testPublishType
	 */	
	private String testPublishType;

	/**
	 * testPublishStatus
	 */	
	private String testPublishStatus;

	/**
	 * testPublishTime
	 */	
	private java.util.Date testPublishTime;

	/**
	 * testTime
	 */	
	private java.util.Date testTime;

	/**
	 * totalTime
	 */	
	private Float totalTime;

	/**
	 * beginTime
	 */	
	private java.util.Date beginTime;

	/**
	 * startTime
	 */	
	private java.util.Date startTime;

	/**
	 * endTime
	 */	
	private java.util.Date endTime;

	/**
	 * startSubmitTime
	 */	
	private java.util.Date startSubmitTime;

	/**
	 * endSubmitTime
	 */	
	private java.util.Date endSubmitTime;

	/**
	 * canBlank
	 */	
	private String canBlank;

	/**
	 * rewardScore
	 */	
	private Float rewardScore;

	/**
	 * sender
	 */	
	private String sender;

	/**
	 * senderName
	 */	
	private String senderName;

	/**
	 * creator
	 */	
	private String creator;

	/**
	 * createTime
	 */	
	private java.util.Date createTime;

	/**
	 * updator
	 */	
	private String updator;

	/**
	 * updateTime
	 */	
	private java.util.Date updateTime;

	/**
	 * delStatus
	 */	
	private String delStatus;

	/**
	 * deliveryId
	 */	
	private String deliveryId;

	/**
	 * examName
	 */	
	private String examName;

	/**
	 * testCostTime
	 */	
	private Float testCostTime;

	/**
	 * appId
	 */	
	private String appId;

	/**
	 * isadepttest
	 */	
	private String isadepttest;
	
	/** 
	 * 批阅状态 
	 */
	private String markStatus;
	
	/**
	 * 得分
	 */
	private Float score;

	/** 允许迟交时长*/
	private Integer delaySubmit;
	
	/** 交卷次数*/
	private Integer resubmit;
	
	/** 派卷来源*/
	private String from;
	
	/** 派卷接收对象信息*/
	private String users;
	
	/** 接收者详情*/
	private List<TestRecipientBO> recipients;

	/** 交卷记录 添加：duffy,判断已批阅的学生不能再次提交  */
	private List<TestUserBO> testUsers;
	
	/** 用户标识*/
	private String userId;
	
	/** 是否交卷 0：没有 1：已交*/
	private String isSubmit;
	
	/** 已交卷人数*/
	private Integer submitCount;
	
	/** 总人数*/
	private Integer totalCount;
	
	/** 已批阅人数*/
	private Integer markCount;

	private List<CodeNameMap> groups;

	/* 派卷学生班级信息*/
	private List<TestRecipientGroupBO> sendPaperGroups;

	/** 指派批阅类型*/
	private String actionType;

	public List<TestRecipientGroupBO> getSendPaperGroups() {
		return sendPaperGroups;
	}

	public void setSendPaperGroups(List<TestRecipientGroupBO> sendPaperGroups) {
		this.sendPaperGroups = sendPaperGroups;
	}
	 /**
	 * @return the testId
	 */		
	public String getTestId() {
		return testId;
	}

	 /**
	 * @param testId the testId to set
	 */	
	public void setTestId(String testId) {
		this.testId = testId;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the activityId
	 */		
	public String getActivityId() {
		return activityId;
	}
	
	 /**
	 * @param activityId the activityId to set
	 */	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	 /**
	 * @return the schoolId
	 */		
	public String getSchoolId() {
		return schoolId;
	}
	
	 /**
	 * @param schoolId the schoolId to set
	 */	
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	 /**
	 * @return the termId
	 */		
	public String getTermId() {
		return termId;
	}
	
	 /**
	 * @param termId the termId to set
	 */	
	public void setTermId(String termId) {
		this.termId = termId;
	}

	 /**
	 * @return the gradeId
	 */		
	public String getGradeId() {
		return gradeId;
	}
	
	 /**
	 * @param gradeId the gradeId to set
	 */	
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	 /**
	 * @return the gradeCode
	 */		
	public String getGradeCode() {
		return gradeCode;
	}
	
	 /**
	 * @param gradeCode the gradeCode to set
	 */	
	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	 /**
	 * @return the gradeName
	 */		
	public String getGradeName() {
		return gradeName;
	}
	
	 /**
	 * @param gradeName the gradeName to set
	 */	
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	 /**
	 * @return the subjectId
	 */		
	public String getSubjectId() {
		return subjectId;
	}
	
	 /**
	 * @param subjectId the subjectId to set
	 */	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	 /**
	 * @return the subjectCode
	 */		
	public String getSubjectCode() {
		return subjectCode;
	}
	
	 /**
	 * @param subjectCode the subjectCode to set
	 */	
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	 /**
	 * @return the subjectName
	 */		
	public String getSubjectName() {
		return subjectName;
	}
	
	 /**
	 * @param subjectName the subjectName to set
	 */	
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	 /**
	 * @return the testName
	 */		
	public String getTestName() {
		return testName;
	}
	
	 /**
	 * @param testName the testName to set
	 */	
	public void setTestName(String testName) {
		this.testName = testName;
	}

	 /**
	 * @return the examId
	 */		
	public String getExamId() {
		return examId;
	}
	
	 /**
	 * @param examId the examId to set
	 */	
	public void setExamId(String examId) {
		this.examId = examId;
	}

	 /**
	 * @return the testPass
	 */		
	public String getTestPass() {
		return testPass;
	}
	
	 /**
	 * @param testPass the testPass to set
	 */	
	public void setTestPass(String testPass) {
		this.testPass = testPass;
	}

	 /**
	 * @return the markType
	 */		
	public String getMarkType() {
		return markType;
	}
	
	 /**
	 * @param markType the markType to set
	 */	
	public void setMarkType(String markType) {
		this.markType = markType;
	}

	 /**
	 * @return the testType
	 */		
	public String getTestType() {
		return testType;
	}
	
	 /**
	 * @param testType the testType to set
	 */	
	public void setTestType(String testType) {
		this.testType = testType;
	}

	 /**
	 * @return the testOrderType
	 */		
	public String getTestOrderType() {
		return testOrderType;
	}
	
	 /**
	 * @param testOrderType the testOrderType to set
	 */	
	public void setTestOrderType(String testOrderType) {
		this.testOrderType = testOrderType;
	}

	 /**
	 * @return the testPublishType
	 */		
	public String getTestPublishType() {
		return testPublishType;
	}
	
	 /**
	 * @param testPublishType the testPublishType to set
	 */	
	public void setTestPublishType(String testPublishType) {
		this.testPublishType = testPublishType;
	}

	 /**
	 * @return the testPublishStatus
	 */		
	public String getTestPublishStatus() {
		return testPublishStatus;
	}
	
	 /**
	 * @param testPublishStatus the testPublishStatus to set
	 */	
	public void setTestPublishStatus(String testPublishStatus) {
		this.testPublishStatus = testPublishStatus;
	}

	 /**
	 * @return the testPublishTime
	 */		
	public java.util.Date getTestPublishTime() {
		return testPublishTime;
	}
	
	 /**
	 * @param testPublishTime the testPublishTime to set
	 */	
	public void setTestPublishTime(java.util.Date testPublishTime) {
		this.testPublishTime = testPublishTime;
	}

	 /**
	 * @return the testTime
	 */		
	public java.util.Date getTestTime() {
		return testTime;
	}
	
	 /**
	 * @param testTime the testTime to set
	 */	
	public void setTestTime(java.util.Date testTime) {
		this.testTime = testTime;
	}

	 /**
	 * @return the totalTime
	 */		
	public Float getTotalTime() {
		return totalTime;
	}
	
	 /**
	 * @param totalTime the totalTime to set
	 */	
	public void setTotalTime(Float totalTime) {
		this.totalTime = totalTime;
	}

	 /**
	 * @return the beginTime
	 */		
	public java.util.Date getBeginTime() {
		return beginTime;
	}
	
	 /**
	 * @param beginTime the beginTime to set
	 */	
	public void setBeginTime(java.util.Date beginTime) {
		this.beginTime = beginTime;
	}

	 /**
	 * @return the startTime
	 */		
	public java.util.Date getStartTime() {
		return startTime;
	}
	
	 /**
	 * @param startTime the startTime to set
	 */	
	public void setStartTime(java.util.Date startTime) {
		this.startTime = startTime;
	}

	 /**
	 * @return the endTime
	 */		
	public java.util.Date getEndTime() {
		return endTime;
	}
	
	 /**
	 * @param endTime the endTime to set
	 */	
	public void setEndTime(java.util.Date endTime) {
		this.endTime = endTime;
	}

	 /**
	 * @return the startSubmitTime
	 */		
	public java.util.Date getStartSubmitTime() {
		return startSubmitTime;
	}
	
	 /**
	 * @param startSubmitTime the startSubmitTime to set
	 */	
	public void setStartSubmitTime(java.util.Date startSubmitTime) {
		this.startSubmitTime = startSubmitTime;
	}

	 /**
	 * @return the endSubmitTime
	 */		
	public java.util.Date getEndSubmitTime() {
		return endSubmitTime;
	}
	
	 /**
	 * @param endSubmitTime the endSubmitTime to set
	 */	
	public void setEndSubmitTime(java.util.Date endSubmitTime) {
		this.endSubmitTime = endSubmitTime;
	}

	 /**
	 * @return the canBlank
	 */		
	public String getCanBlank() {
		return canBlank;
	}
	
	 /**
	 * @param canBlank the canBlank to set
	 */	
	public void setCanBlank(String canBlank) {
		this.canBlank = canBlank;
	}

	 /**
	 * @return the rewardScore
	 */		
	public Float getRewardScore() {
		return rewardScore;
	}
	
	 /**
	 * @param rewardScore the rewardScore to set
	 */	
	public void setRewardScore(Float rewardScore) {
		this.rewardScore = rewardScore;
	}

	 /**
	 * @return the sender
	 */		
	public String getSender() {
		return sender;
	}
	
	 /**
	 * @param sender the sender to set
	 */	
	public void setSender(String sender) {
		this.sender = sender;
	}

	 /**
	 * @return the senderName
	 */		
	public String getSenderName() {
		return senderName;
	}
	
	 /**
	 * @param senderName the senderName to set
	 */	
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	 /**
	 * @return the creator
	 */		
	public String getCreator() {
		return creator;
	}
	
	 /**
	 * @param creator the creator to set
	 */	
	public void setCreator(String creator) {
		this.creator = creator;
	}

	 /**
	 * @return the createTime
	 */		
	public java.util.Date getCreateTime() {
		return createTime;
	}
	
	 /**
	 * @param createTime the createTime to set
	 */	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	 /**
	 * @return the updator
	 */		
	public String getUpdator() {
		return updator;
	}
	
	 /**
	 * @param updator the updator to set
	 */	
	public void setUpdator(String updator) {
		this.updator = updator;
	}

	 /**
	 * @return the updateTime
	 */		
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	
	 /**
	 * @param updateTime the updateTime to set
	 */	
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	 /**
	 * @return the delStatus
	 */		
	public String getDelStatus() {
		return delStatus;
	}
	
	 /**
	 * @param delStatus the delStatus to set
	 */	
	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	 /**
	 * @return the deliveryId
	 */		
	public String getDeliveryId() {
		return deliveryId;
	}
	
	 /**
	 * @param deliveryId the deliveryId to set
	 */	
	public void setDeliveryId(String deliveryId) {
		this.deliveryId = deliveryId;
	}

	 /**
	 * @return the examName
	 */		
	public String getExamName() {
		return examName;
	}
	
	 /**
	 * @param examName the examName to set
	 */	
	public void setExamName(String examName) {
		this.examName = examName;
	}

	 /**
	 * @return the testCostTime
	 */		
	public Float getTestCostTime() {
		return testCostTime;
	}
	
	 /**
	 * @param testCostTime the testCostTime to set
	 */	
	public void setTestCostTime(Float testCostTime) {
		this.testCostTime = testCostTime;
	}

	 /**
	 * @return the appId
	 */		
	public String getAppId() {
		return appId;
	}
	
	 /**
	 * @param appId the appId to set
	 */	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	 /**
	 * @return the isadepttest
	 */		
	public String getIsadepttest() {
		return isadepttest;
	}
	
	 /**
	 * @param isadepttest the isadepttest to set
	 */	
	public void setIsadepttest(String isadepttest) {
		this.isadepttest = isadepttest;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getDelaySubmit() {
		return delaySubmit;
	}

	public void setDelaySubmit(Integer delaySubmit) {
		this.delaySubmit = delaySubmit;
	}

	public Integer getResubmit() {
		return resubmit;
	}

	public void setResubmit(Integer resubmit) {
		this.resubmit = resubmit;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getUsers() {
		return users;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public List<TestRecipientBO> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<TestRecipientBO> recipients) {
		this.recipients = recipients;
	}

	public List<TestUserBO> getTestUsers() {
		return testUsers;
	}

	public void setTestUsers(List<TestUserBO> testUsers) {
		this.testUsers = testUsers;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}

	public Integer getSubmitCount() {
		return submitCount;
	}

	public void setSubmitCount(Integer submitCount) {
		this.submitCount = submitCount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getMarkCount() {
		return markCount;
	}

	public void setMarkCount(Integer markCount) {
		this.markCount = markCount;
	}

	public String getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}

	public List<CodeNameMap> getGroups() {
		return groups;
	}

	public void setGroups(List<CodeNameMap> groups) {
		this.groups = groups;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	@Override
	public String toString() {
		return "ExamBO{" +
				"testId='" + testId + '\'' +
				", activityId='" + activityId + '\'' +
				", schoolId='" + schoolId + '\'' +
				", schoolName='" + schoolName + '\'' +
				", termId='" + termId + '\'' +
				", termName='" + termName + '\'' +
				", gradeId='" + gradeId + '\'' +
				", gradeCode='" + gradeCode + '\'' +
				", gradeName='" + gradeName + '\'' +
				", subjectId='" + subjectId + '\'' +
				", subjectCode='" + subjectCode + '\'' +
				", subjectName='" + subjectName + '\'' +
				", testName='" + testName + '\'' +
				", examId='" + examId + '\'' +
				", testPass='" + testPass + '\'' +
				", markType='" + markType + '\'' +
				", testType='" + testType + '\'' +
				", testOrderType='" + testOrderType + '\'' +
				", testPublishType='" + testPublishType + '\'' +
				", testPublishStatus='" + testPublishStatus + '\'' +
				", testPublishTime=" + testPublishTime +
				", testTime=" + testTime +
				", totalTime=" + totalTime +
				", beginTime=" + beginTime +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", startSubmitTime=" + startSubmitTime +
				", endSubmitTime=" + endSubmitTime +
				", canBlank='" + canBlank + '\'' +
				", rewardScore=" + rewardScore +
				", sender='" + sender + '\'' +
				", senderName='" + senderName + '\'' +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", updator='" + updator + '\'' +
				", updateTime=" + updateTime +
				", delStatus='" + delStatus + '\'' +
				", deliveryId='" + deliveryId + '\'' +
				", examName='" + examName + '\'' +
				", testCostTime=" + testCostTime +
				", appId='" + appId + '\'' +
				", isadepttest='" + isadepttest + '\'' +
				", markStatus='" + markStatus + '\'' +
				", score=" + score +
				", delaySubmit=" + delaySubmit +
				", resubmit=" + resubmit +
				", from='" + from + '\'' +
				", users='" + users + '\'' +
				", recipients=" + recipients +
				", userId='" + userId + '\'' +
				", isSubmit='" + isSubmit + '\'' +
				", submitCount=" + submitCount +
				", totalCount=" + totalCount +
				", markCount=" + markCount +
				'}';
	}
}