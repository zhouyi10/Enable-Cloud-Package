package com.enableets.edu.pakage.framework.ppr.bo;

import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
public class ExamInfoBO {

  private String examId;

  private String examName;

  private String gradeId;

  private String subjectId;

  private String examType;

  private String materialVersionId;

  private Float score;

  private Long answerCostTime;

  private String description;

  private String delStatus;

  private String contentId;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;

  private String gradeCode;

  private String gradeName;

  private String subjectCode;

  private String subjectName;

  private String diffcult;

  private String schoolCode;

  private String schoolName;

  private String termId;

  private String termName;

  @Transient
  private List<ExamKindInfoBO> examKinds;
}
