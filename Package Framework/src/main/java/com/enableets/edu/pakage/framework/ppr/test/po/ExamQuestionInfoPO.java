package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Entity
@Table(name = "exam_question_info")
@Data
public class ExamQuestionInfoPO {

  @Column(name = "exam_question_id")
  private String examQuestionId;

  @Column(name = "exam_question_type_id")
  private String examQuestionTypeId;

  @Column(name = "question_no")
  private String questionNo;

  @Column(name = "question_order")
  private Integer questionOrder;

  @Column(name = "score")
  private Float score;

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

  @Transient
  private List<ExamQuestionChildInfoPO> examQuestionChildren;
}
