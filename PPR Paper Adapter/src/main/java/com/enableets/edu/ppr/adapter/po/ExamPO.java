package com.enableets.edu.ppr.adapter.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "t_as_exam")
public class ExamPO {

  @Column(name = "exam_id")
  private String examId;

  @Column(name = "exam_template_id")
  private String examTemplateId;

  @Column(name = "exam_name")
  private String examName;

  @Column(name = "grade_id")
  private String gradeId;

  @Column(name = "subject_id")
  private String subjectId;

  @Column(name = "exam_type")
  private String examType;

  @Column(name = "material_version_id")
  private String materialVersionId;

  @Column(name = "score")
  private Float score;

  @Column(name = "answer_cost_time")
  private Long answerCostTime;

  @Column(name = "description")
  private String description;

  @Column(name = "del_status")
  private String delStatus;

  @Column(name = "content_id")
  private String contentId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

  @Column(name = "grade_code")
  private String gradeCode;

  @Column(name = "grade_name")
  private String gradeName;

  @Column(name = "subject_code")
  private String subjectCode;

  @Column(name = "subject_name")
  private String subjectName;

  @Column(name = "diffcult")
  private String diffcult;

  @Column(name = "ability_id")
  private String abilityId;

  @Column(name = "school_code")
  private String schoolCode;

  @Column(name = "school_name")
  private String schoolName;

  @Column(name = "term_id")
  private String termId;

  @Column(name = "term_name")
  private String termName;

  @Column(name = "use_type")
  private String useType;

}
