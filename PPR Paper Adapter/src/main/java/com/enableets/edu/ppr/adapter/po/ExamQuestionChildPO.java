package com.enableets.edu.ppr.adapter.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "t_as_exam_question_child")
public class ExamQuestionChildPO {

  @Column(name = "exam_question_child_id")
  private String examQuestionChildId;

  @Column(name = "exam_question_id")
  private String examQuestionId;

  @Column(name = "question_child_no")
  private String questionChildNo;

  @Column(name = "score")
  private Float score;

  @Column(name = "question_child_id")
  private String questionChildId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;
}
