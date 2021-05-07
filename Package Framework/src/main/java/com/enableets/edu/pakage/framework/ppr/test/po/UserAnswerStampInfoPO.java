package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

/**
 * Answer time record table
 */
@Entity
@Table(name = "user_answer_stamp_info")
@Data
public class UserAnswerStampInfoPO {

  /** */
  @Column(name = "answer_stamp_id")
  private String answerStampId;

  /** */
  @Column(name = "answer_id")
  private String answerId;

  /** */
  @Column(name = "begin_time")
  private java.util.Date beginTime;

  /** */
  @Column(name = "end_time")
  private java.util.Date endTime;

  /** */
  @Column(name = "last_time")
  private Long lastTime;

  /** */
  @Column(name = "question_id")
  private String questionId;

  /** */
  @Column(name = "test_id")
  private String testId;

  /** */
  @Column(name = "exam_id")
  private String examId;

  /** */
  @Column(name = "creator")
  private String creator;

  /** */
  @Column(name = "create_time")
  private java.util.Date createTime;

  /** */
  @Column(name = "updator")
  private String updator;

  /** */
  @Column(name = "update_time")
  private java.util.Date updateTime;
}
