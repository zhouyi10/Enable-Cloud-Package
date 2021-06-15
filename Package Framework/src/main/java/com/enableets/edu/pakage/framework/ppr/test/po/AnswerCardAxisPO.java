package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name = "answer_card_axis")
public class AnswerCardAxisPO {

  @Column(name = "axis_id")
  private Long axisId;

  @Column(name = "answer_card_id")
  private Long answerCardId;

  @Column(name = "node_id")
  private Long nodeId;

  @Column(name = "question_id")
  private Long questionId;

  @Column(name = "parent_node_id")
  private Long parentNodeId;

  @Column(name = "parent_id")
  private Long parentId;

  @Column(name = "sequencing")
  private Long sequencing;

  @Column(name = "x_axis")
  private Double xAxis;

  @Column(name = "y_axis")
  private Double yAxis;

  @Column(name = "width")
  private Double width;

  @Column(name = "height")
  private Double height;

  @Column(name = "page_no")
  private Integer pageNo;

  @Column(name = "type_code")
  private String typeCode;

  @Column(name = "type_name")
  private String typeName;

  @Column(name = "option_count")
  private Long optionCount;

  @Column(name = "row_count")
  private Long rowCount;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private Date updateTime;

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
