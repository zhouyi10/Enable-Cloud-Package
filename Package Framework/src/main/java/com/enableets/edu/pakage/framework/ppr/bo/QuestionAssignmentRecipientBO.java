package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * 分题批阅对应学生
 */
@Data
public class QuestionAssignmentRecipientBO {

  private String assignmentId;

  private String userId;

  private String fullName;

  private String creator;

  private java.util.Date createTime;

  private String updator;

  private java.util.Date updateTime;
}
