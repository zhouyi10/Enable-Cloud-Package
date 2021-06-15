package com.enableets.edu.pakage.framework.ppr.test.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "answer_request_data")
public class AnswerRequestDataPO {

  @Column(name = "answer_request_id")
  private String answerRequestId;

  @Column(name = "origin_data")
  private String originData;

  @Column(name = "status")
  private Integer status;

  @Column(name = "retry_times")
  private Integer retryTimes;

  @Column(name = "error_code")
  private String errorCode;

  @Column(name = "error_message")
  private String errorMessage;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;
}
