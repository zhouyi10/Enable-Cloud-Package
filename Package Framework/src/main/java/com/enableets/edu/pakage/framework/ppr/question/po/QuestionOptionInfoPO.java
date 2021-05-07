package com.enableets.edu.pakage.framework.ppr.question.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "t_as_question_option")
@Data
public class QuestionOptionInfoPO {

  @Column(name = "option_id")
  private String optionId;

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "option_title")
  private String optionTitle;

  @Column(name = "option_content")
  private String optionContent;

  @Column(name = "option_order")
  private Integer optionOrder;

  @Column(name = "option_score")
  private String optionScore;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

}
