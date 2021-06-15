package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "question_axis_info")
public class QuestionAxisInfoPO {

  @Column(name = "axis_id")
  private String axisId;

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "parent_id")
  private String parentId;

  @Column(name = "x_axis")
  private Double xAxis;

  @Column(name = "y_axis")
  private Double yAxis;

  @Column(name = "width")
  private Double width;

  @Column(name = "height")
  private Double height;

  @Column(name = "sequence")
  private Integer sequence;

  @Column(name = "file_id")
  private String fileId;

  @Column(name = "file_name")
  private String fileName;

  @Column(name = "url")
  private String url;

  @Column(name = "file_ext")
  private String fileExt;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

  public Double getxAxis() {
    return xAxis;
  }

  public void setxAxis(Double xAxis) {
    this.xAxis = xAxis;
  }

  public Double getyAxis() {
    return yAxis;
  }

  public void setyAxis(Double yAxis) {
    this.yAxis = yAxis;
  }
}
