package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Table(name = "subject_ability_info")
@Entity
@Data
public class SubjectAbilityInfoPO {

  @Column(name = "subject_ability_id")
  private String subjectAbilityId;

  @Column(name = "subject_id")
  private String subjectId;

  @Column(name = "ability_id")
  private String abilityId;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

}
