package com.enableets.edu.pakage.manager.ppr.bo;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * test_user
 */
@Data
public class TestUserInfoBO implements Serializable {

	private static final long serialVersionUID = -7537136272683054207L;

	/**
	 * testUserId
	 */
	private String testUserId;

	/**
	 * activityId
	 */
	private String activityId;

	/**
	 * schoolId
	 */
	private String schoolId;

	/**
	 * termId
	 */
	private String termId;

	/**
	 * gradeId
	 */
	private String gradeCode;

	/**
	 * classId
	 */
	private String classId;

	/**
	 * groupId
	 */
	private String groupId;

	/**
	 * groupName
	 */
	private String groupName;

	/**
	 * userId
	 */
	private String userId;

	/**
	 * userName
	 */
	private String userName;

	/**
	 * testId
	 */
	private String testId;

	/**
	 * answerCostTime
	 */
	private Long answerCostTime;

	/**
	 * userScore
	 */
	private Float userScore;

	/**
	 * teacherNote
	 */
	private String teacherNote;

	/**
	 * rewardNum
	 */
	private Long rewardNum;

	/**
	 * submitStatus
	 */
	private String submitStatus;

	/**
	 * submitTime
	 */
	private Date submitTime;

	/**
	 * absentStatus
	 */
	private String absentStatus;

	/**
	 * testUserStatus
	 */
	private String testUserStatus;

	/**
	 * startAnswerTime
	 */
	private Date startAnswerTime;

	/**
	 * endAnswerTime
	 */
	private Date endAnswerTime;

	/**
	 * markStatus
	 */
	private String markStatus;

	/**
	 * listenInfo
	 */
	private String listenInfo;

	/**
	 * testTimes
	 */
	private Long testTimes;

	/**
	 * creator
	 */
	private String creator;

	/**
	 * createTime
	 */
	private Date createTime;

	/**
	 * updator
	 */
	private String updator;

	/**
	 * updateTime
	 */
	private Date updateTime;

	/**
	 * 考试名称
	 */
	private String testName;

	/**
	 * 考试活动标识
	 */
	private String tActivityId;

	/** 答案*/
	private List<UserAnswerInfoBO> answers;

}
