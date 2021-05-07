package com.enableets.edu.ppr.adapter.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_as_exam_question")
public class ExamQuestionPO {

  @Column(name = "exam_question_id")
  private String examQuestionId;

  @Column(name = "exam_question_type_id")
  private String examQuestionTypeId;

  @Column(name = "question_no")
  private String questionNo;

  @Column(name = "question_order")
  private Long questionOrder;

  @Column(name = "score")
  private Double score;

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

}
