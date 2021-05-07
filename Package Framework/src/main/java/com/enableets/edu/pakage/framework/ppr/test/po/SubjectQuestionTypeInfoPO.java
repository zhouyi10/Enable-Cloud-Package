package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "subject_question_type_info")
public class SubjectQuestionTypeInfoPO {

  @Column(name = "subject_question_type_id")
  private String subjectQuestionTypeId;

  @Column(name = "subject_id")
  private String subjectId;

  @Column(name = "question_type_id")
  private String questionTypeId;

  @Column(name = "question_type_order")
  private Long questionTypeOrder;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

}
