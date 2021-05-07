package com.enableets.edu.ppr.adapter.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "t_as_exam_kind")
public class ExamKindPO {

  @Column(name = "exam_kind_id")
  private String examKindId;

  @Column(name = "exam_id")
  private String examId;

  @Column(name = "kind_no")
  private Integer kindNo;

  @Column(name = "score")
  private Float score;

  @Column(name = "description")
  private String description;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

}
