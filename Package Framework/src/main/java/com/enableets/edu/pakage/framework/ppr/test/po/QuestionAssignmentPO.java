package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "question_assignment")
public class QuestionAssignmentPO {

  @Column(name = "assignment_id")
  private String assignmentId;

  @Column(name = "test_id")
  private String testId;

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "sequence")
  private Long sequence;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

  @Transient
  private List<QuestionAssignmentRecipientPO> recipients;

  @Transient
  private Integer submittedCount;

  @Transient
  private Integer totalCount;

}
