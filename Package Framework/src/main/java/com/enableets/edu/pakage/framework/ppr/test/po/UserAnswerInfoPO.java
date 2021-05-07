package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

/**
 * user_answer_info
 */
@Entity
@Table(name = "user_answer_info")
@Data
public class UserAnswerInfoPO {

	@Column(name="answer_id")
	private String answerId;

	@Column(name="parent_id")
	private String parentId;

	@Column(name="test_user_id")
	private String testUserId;

	@Column(name="user_id")
	private String userId;

	@Column(name="test_id")
	private String testId;

	@Column(name="exam_id")
	private String examId;

	@Column(name="question_id")
	private String questionId;

	@Column(name="user_answer")
	private String userAnswer;

	@Column(name="answer_score")
	private Float answerScore;

	@Column(name="answer_status")
	private String answerStatus;

	@Column(name="check_status")
	private String checkStatus;

	@Column(name="teacher_note")
	private String teacherNote;

	@Column(name="answer_cost_time")
	private Long answerCostTime;

	@Column(name="mark_status")
	private String markStatus;

	@Column(name="creator")
	private String creator;

	@Column(name="create_time")
	private java.util.Date createTime;

	@Column(name="updator")
	private String updator;

	@Column(name="update_time")
	private java.util.Date updateTime;

	@Column(name="question_score")
	private Float questionScore;

	@Transient
	private List<UserAnswerStampInfoPO> answerStamps;

	@Transient
	private List<UserAnswerCanvasInfoPO> canvases;
}