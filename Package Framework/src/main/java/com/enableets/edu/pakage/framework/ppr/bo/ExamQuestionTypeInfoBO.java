package com.enableets.edu.pakage.framework.ppr.bo;

import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
public class ExamQuestionTypeInfoBO {

  private String examQuestionTypeId;

  private String examKindId;

  private Integer questionTypeNo;

  private String description;

  private Float score;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;

  @Transient
  private List<ExamQuestionInfoBO> examQuestions;
}
