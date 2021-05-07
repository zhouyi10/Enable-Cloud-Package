package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "activity_assigner")
public class ActivityAssignerPO {

  @Column(name = "activity_id")
  private String activityId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "action")
  private String action;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;
}
