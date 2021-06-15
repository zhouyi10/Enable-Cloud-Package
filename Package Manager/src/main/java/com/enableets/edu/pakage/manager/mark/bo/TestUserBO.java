package com.enableets.edu.pakage.manager.mark.bo;


/**
 * 交卷对象
 *
 * @author caleb_liu@enable-ets.com
 * @since 2018/12/28
 */
public class TestUserBO implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	/** 主键标识*/
	private String testUserId;

	/** 交卷活动标识*/
	private String activityId;

	/** 学校标识*/
	private String schoolId;

	/** 学期标识*/
	private String termId;

	/** 年级编码*/
	private String gradeCode;

	/** 班级标识*/
	private String classId;

	/** 用户标识*/
	private String userId;

	/** 用户名称*/
	private String userName;

	/** 考试标识*/
	private String testId;

	/** 考试耗时*/
	private Long answerCostTime;

	/** 学生得分*/
	private Float userScore;

	/** 老师留言*/
	private String teacherNote;

	/** 交卷状态*/
	private String submitStatus;

	/** 交卷时间*/
	private java.util.Date submitTime;

	/** 开始作答时间*/
	private java.util.Date startAnswerTime;

	/** 结束作答时间*/
	private java.util.Date endAnswerTime;

	/** 批阅状态*/
	private String markStatus;

	/** 听力文件*/
	private String listenInfo;

	/** 考试次数*/
	private Long testTimes;

	/** 创建者*/
	private String creator;

	/** 创建时间*/
	private java.util.Date createTime;

	/** 更新者*/
	private String updator;

	/** 更新时间*/
	private java.util.Date updateTime;

	public String getTestUserId() {
		return testUserId;
	}

	public void setTestUserId(String testUserId) {
		this.testUserId = testUserId;
	}

	public String getActivityId() {
		return activityId;
	}
	
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getSchoolId() {
		return schoolId;
	}
	
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getTermId() {
		return termId;
	}
	
	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getClassId() {
		return classId;
	}
	
	public void setClassId(String classId) {
		this.classId = classId;
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

	public String getTestId() {
		return testId;
	}
	
	public void setTestId(String testId) {
		this.testId = testId;
	}

	public Long getAnswerCostTime() {
		return answerCostTime;
	}
	
	public void setAnswerCostTime(Long answerCostTime) {
		this.answerCostTime = answerCostTime;
	}


	public Float getUserScore() {
		return userScore;
	}
	
	public void setUserScore(Float userScore) {
		this.userScore = userScore;
	}

	public String getTeacherNote() {
		return teacherNote;
	}
	
	public void setTeacherNote(String teacherNote) {
		this.teacherNote = teacherNote;
	}

	public String getSubmitStatus() {
		return submitStatus;
	}
	
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}

	public java.util.Date getSubmitTime() {
		return submitTime;
	}
	
	public void setSubmitTime(java.util.Date submitTime) {
		this.submitTime = submitTime;
	}

	public java.util.Date getStartAnswerTime() {
		return startAnswerTime;
	}
	
	public void setStartAnswerTime(java.util.Date startAnswerTime) {
		this.startAnswerTime = startAnswerTime;
	}

	public java.util.Date getEndAnswerTime() {
		return endAnswerTime;
	}
	
	public void setEndAnswerTime(java.util.Date endAnswerTime) {
		this.endAnswerTime = endAnswerTime;
	}

	public String getMarkStatus() {
		return markStatus;
	}
	
	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}

	public String getListenInfo() {
		return listenInfo;
	}
	
	public void setListenInfo(String listenInfo) {
		this.listenInfo = listenInfo;
	}

	public Long getTestTimes() {
		return testTimes;
	}
	
	public void setTestTimes(Long testTimes) {
		this.testTimes = testTimes;
	}

	public String getCreator() {
		return creator;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdator() {
		return updator;
	}
	
	public void setUpdator(String updator) {
		this.updator = updator;
	}

	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "TestUserBO{" +
				"testUserId='" + testUserId + '\'' +
				", activityId='" + activityId + '\'' +
				", schoolId='" + schoolId + '\'' +
				", termId='" + termId + '\'' +
				", gradeCode='" + gradeCode + '\'' +
				", classId='" + classId + '\'' +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", testId='" + testId + '\'' +
				", answerCostTime=" + answerCostTime +
				", userScore=" + userScore +
				", teacherNote='" + teacherNote + '\'' +
				", submitStatus='" + submitStatus + '\'' +
				", submitTime=" + submitTime +
				", startAnswerTime=" + startAnswerTime +
				", endAnswerTime=" + endAnswerTime +
				", markStatus='" + markStatus + '\'' +
				", listenInfo='" + listenInfo + '\'' +
				", testTimes=" + testTimes +
				", creator='" + creator + '\'' +
				", createTime=" + createTime +
				", updator='" + updator + '\'' +
				", updateTime=" + updateTime +
				'}';
	}
}
