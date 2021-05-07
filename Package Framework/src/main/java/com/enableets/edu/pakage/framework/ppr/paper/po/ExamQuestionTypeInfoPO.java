package com.enableets.edu.pakage.framework.ppr.paper.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Entity
@Table(name = "exam_question_type_info")
@Data
public class ExamQuestionTypeInfoPO {

  @Column(name = "exam_question_type_id")
  private String examQuestionTypeId;

  @Column(name = "exam_kind_id")
  private String examKindId;

  @Column(name = "question_type_no")
  private Integer questionTypeNo;

  @Column(name = "description")
  private String description;

  @Column(name = "score")
  private Float score;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

  @Transient
  private List<ExamQuestionInfoPO> examQuestions;
}
