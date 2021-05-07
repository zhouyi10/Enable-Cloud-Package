package com.enableets.edu.pakage.framework.ppr.bo;

import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
public class ExamKindInfoBO {

  private String examKindId;

  private String examId;

  private Integer kindNo;

  private Float score;

  private String description;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;

  @Transient
  private List<ExamQuestionTypeInfoBO> examQuestionTypes;
}
