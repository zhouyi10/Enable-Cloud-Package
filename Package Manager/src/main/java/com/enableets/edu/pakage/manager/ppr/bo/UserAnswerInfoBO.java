package com.enableets.edu.pakage.manager.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * user_answer_info
 */
@Data
public class UserAnswerInfoBO {
	
	/**
	 * answerId
	 */	
	private String answerId;

	/**
	 * parentId
	 */
	private String parentId;

	/**
	 * testUserId
	 */
	private String testUserId;

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
	 * test Name
	 */
	private String testName;

	/**
	 * examId
	 */
	private String examId;

	/**
	 * exam Name
	 */
	private String examName;

	/**
	 * Score
	 */
	private Float score;

	/**
	 * questionId
	 */
	private String questionId;

	/**
	 * userAnswer
	 */
	private String userAnswer;

	/**
	 * answerScore
	 */
	private Float answerScore;

	/**
	 * answerStatus
	 */
	private String answerStatus;

	/**
	 * checkStatus
	 */
	private String checkStatus;

	/**
	 * teacherNote
	 */
	private String teacherNote;

	/**
	 * answerCostTime
	 */
	private Long answerCostTime;

	/**
	 * markStatus
	 */
	private String markStatus;

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
	 * questionScore
	 */
	private Float questionScore;

	/**
	 *  canvas info
	 */
	private List<UserAnswerCanvasInfoBO> canvases;

}
