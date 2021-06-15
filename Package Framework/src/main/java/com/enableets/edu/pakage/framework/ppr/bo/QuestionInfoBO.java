package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Question storage question info
 */
@Data
public class QuestionInfoBO {

  private String questionId;

  private String parentId;

  private String subjectId;

  private String questionTypeId;

  private String diagnose;

  private String listen;

  private String questionDifficulty;

  private String abilityId;

  private Float score;

  private String source;

  private String questionContent;

  private String questionContentNoHtml;

  private String answer;

  private String answerContent;

  private String answerAnalyse;

  private Integer optionCount;

  private Integer childCount;

  private String publisher;

  private Integer questionChildOrder;

  private String affixId;

  private String creator;

  private Date createTime;

  private String updator;

  private Date updateTime;

  private String questionNo;

  private String diagnosisKnowledge;

  private String diagnosisKnowledgeDetail;

  private String delStatus;

  private String questionKind;

  private String contentId;

  private String gradeId;

  private String gradeName;

  private String subjectName;

  private String schoolCode;

  private String schoolName;

  private String termCode;

  private String termName;

  private String questionSource;

  private String quoteStatus;

  private List<QuestionKnowledgeInfoBO> knowledges;

  private List<QuestionOptionInfoBO> options;

  private List<QuestionAxisBO> axises;
}
