package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * t_as_test_user
 */
@Data
@Entity
@Table(name = "test_user_info")
public class TestUserInfoPO {

	@Id
	@Column(name="test_user_id")
	private String testUserId;

	@Column(name="activity_id")
	private String activityId;

	@Column(name="school_id")
	private String schoolId;

	@Column(name="school_name")
	private String schoolName;

	@Column(name="term_id")
	private String termId;

	@Column(name="class_id")
	private String classId;

	@Column(name="group_id")
	private String groupId;

	@Column(name="group_name")
	private String groupName;

	@Column(name="grade_id")
	private String gradeId;

	@Column(name="grade_name")
	private String gradeName;

	@Column(name="user_id")
	private String userId;

	@Column(name="user_name")
	private String userName;

	@Column(name="test_id")
	private String testId;

	@Column(name="answer_cost_time")
	private Long answerCostTime;

	@Column(name="user_score")
	private Float userScore;

	@Column(name="teacher_note")
	private String teacherNote;

	@Column(name = "reward_num")
	private Integer rewardNum;

	@Column(name="submit_status")
	private String submitStatus;

	@Column(name="submit_time")
	private Date submitTime;

	@Column(name="start_answer_time")
	private Date startAnswerTime;

	@Column(name="end_answer_time")
	private Date endAnswerTime;

	@Column(name="mark_status")
	private String markStatus;

	@Column(name="listen_info")
	private String listenInfo;

	@Column(name="creator")
	private String creator;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="updator")
	private String updator;

	@Column(name="update_time")
	private Date updateTime;

	@Transient
	private String testName;

	@Transient
	private String examId;

	@Transient
	private String examName;

	@Transient
	private Float score;

	@Transient
	private List<UserAnswerInfoPO> answers;

}
