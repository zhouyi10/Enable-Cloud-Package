package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * The corresponding students
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryQuestionAssignmentRecipientResultDTO {

  /** Assign ID*/
  private String assignmentId;

  /** User ID*/
  private String userId;

  /** User Name*/
  private String fullName;

  /** Creator*/
  private String creator;

  /** Create Time*/
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private java.util.Date createTime;

  /** Updater*/
  private String updator;

  /** Update Time*/
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private java.util.Date updateTime;
}
