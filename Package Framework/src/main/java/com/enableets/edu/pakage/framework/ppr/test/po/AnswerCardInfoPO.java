package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "answer_card_info")
public class AnswerCardInfoPO {

  @Column(name = "answer_card_id")
  private Long answerCardId;

  @Column(name = "exam_id")
  private Long examId;

  @Column(name = "column_type")
  private Integer columnType;

  @Column(name = "candidate_number_edition")
  private Integer candidateNumberEdition;

  @Column(name = "page_type")
  private String pageType;

  @Column(name = "judge_style")
  private String judgeStyle;

  @Column(name = "sealing_line_status")
  private String sealingLineStatus;

  @Column(name = "question_content_status")
  private String questionContentStatus;

  @Column(name = "page_count")
  private Integer pageCount;

  @Column(name = "status")
  private Integer status;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;
}
