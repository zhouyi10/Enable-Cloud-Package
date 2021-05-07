package com.enableets.edu.pakage.framework.ppr.bo;

import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
public class ExamQuestionInfoBO {

  private String examQuestionId;

  private String examQuestionTypeId;

  private String questionNo;

  private Integer questionOrder;

  private Float score;

  private String questionId;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;

  private QuestionInfoBO question;

  @Transient
  private List<ExamQuestionChildInfoBO> examQuestionChildren;
}
