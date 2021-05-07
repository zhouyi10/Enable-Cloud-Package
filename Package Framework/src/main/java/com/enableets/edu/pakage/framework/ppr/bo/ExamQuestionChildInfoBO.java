package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

@Data
public class ExamQuestionChildInfoBO {

  private String examQuestionChildId;

  private String examQuestionId;

  private String questionChildNo;

  private Float score;

  private String questionChildId;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;

  private QuestionInfoBO question;

}
