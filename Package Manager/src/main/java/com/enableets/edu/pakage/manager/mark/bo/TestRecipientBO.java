package com.enableets.edu.pakage.manager.mark.bo;

/**
 * 考试接收对象BO
 * @author walle_yu@enable-ets.com
 * @since 2018年5月31日
 */
public class TestRecipientBO {
	
	/** 考试标识*/
	private Long testId;
	
	/** 用户标识*/
	private String userId;
	
	/** 用户名称*/
	private String userName;
	
	/** 学校标识*/
	private String schoolId;
	
	/** 学校名称*/
	private String schoolName;
	
	/** 学期标识*/
	private String termId;
	
	/** 学期名称*/
	private String termName;
	
	/** 年级编码*/
	private String gradeCode;
	
	/** 年级名称*/
	private String gradeName;
	
	/** 班级标识*/
	private String classId;
	
	/** 班级名称*/
	private String className;

	/** 群组标识*/
	private String groupId;

	/** 群组名称*/
	private String groupName;

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getTermId() {
		return termId;
	}

	public void setTermId(String termId) {
		this.termId = termId;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}

	public String getGradeCode() {
		return gradeCode;
	}

	public void setGradeCode(String gradeCode) {
		this.gradeCode = gradeCode;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public String toString() {
		return "ExamRecipientBO{" +
				"testId=" + testId +
				", userId='" + userId + '\'' +
				", userName='" + userName + '\'' +
				", schoolId='" + schoolId + '\'' +
				", schoolName='" + schoolName + '\'' +
				", termId='" + termId + '\'' +
				", termName='" + termName + '\'' +
				", gradeCode='" + gradeCode + '\'' +
				", gradeName='" + gradeName + '\'' +
				", classId='" + classId + '\'' +
				", className='" + className + '\'' +
				", groupId='" + groupId + '\'' +
				", groupName='" + groupName + '\'' +
				'}';
	}
}
