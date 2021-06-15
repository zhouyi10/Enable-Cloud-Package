package com.enableets.edu.pakage.framework.ppr.question.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Question storage question info
 */
@Entity
@Table(name = "t_as_question")
@Data
public class QuestionInfoPO {

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "parent_id")
  private String parentId;

  @Column(name = "subject_id")
  private String subjectId;

  @Column(name = "question_type_id")
  private String questionTypeId;

  @Column(name = "listen")
  private String listen;

  @Column(name = "question_difficulty")
  private String questionDifficulty;

  @Column(name = "ability_id")
  private String abilityId;

  @Column(name = "score")
  private Float score;

  @Column(name = "source")
  private String source;

  @Column(name = "question_content")
  private String questionContent;

  @Column(name = "question_content_no_html")
  private String questionContentNoHtml;

  @Column(name = "answer")
  private String answer;

  @Column(name = "answer_content")
  private String answerContent;

  @Column(name = "answer_analyse")
  private String answerAnalyse;

  @Column(name = "option_count")
  private Integer optionCount;

  @Column(name = "child_count")
  private Integer childCount;

  /** Question Answer Cost Time*/
  @Column(name = "estimate_time")
  private Float estimateTime;

  @Column(name = "publisher")
  private String publisher;

  @Column(name = "questionChildOrder")
  private Integer question_child_order;

  @Column(name = "affix_id")
  private String affixId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "question_no")
  private String questionNo;

  @Column(name = "question_kind")
  private String questionKind;

  @Column(name = "content_id")
  private String contentId;

  @Column(name = "grade_id")
  private String gradeId;

  @Column(name = "grade_name")
  private String gradeName;

  @Column(name = "subject_name")
  private String subjectName;

  @Column(name = "school_code")
  private String schoolCode;

  @Column(name = "school_name")
  private String schoolName;

  @Column(name = "term_code")
  private String termCode;

  @Column(name = "term_name")
  private String termName;

  @Column(name = "question_source")
  private String questionSource;

  @Transient
  private List<QuestionOptionInfoPO> options;
}
