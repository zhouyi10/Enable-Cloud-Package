package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * The Marking students
 */
@Data
@ApiModel(value = "QueryQuestionAssignmentRecipientResultVO", description = "The Marking students ")
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryQuestionAssignmentRecipientResultVO {

  /** Assignment ID*/
  @ApiModelProperty(value = "Assignment ID")
  private String assignmentId;

  /** User ID*/
  @ApiModelProperty(value = "User ID")
  private String userId;

  /** User Name*/
  @ApiModelProperty(value = "User Name")
  private String fullName;

  /** Creator*/
  @ApiModelProperty(value = "Creator")
  private String creator;

  /** Create Time*/
  @ApiModelProperty(value = "Create Time")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private java.util.Date createTime;

  /** Updater */
  @ApiModelProperty(value = "Updater")
  private String updator;

  /** Update Time*/
  @ApiModelProperty(value = "Update Time")
  @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
  @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  private java.util.Date updateTime;
}
