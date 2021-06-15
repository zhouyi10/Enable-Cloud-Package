package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Add Coordinate Info About Answer Card
 * @author walle_yu@enable-ets.com
 * @since 2019/11/05
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AnswerCardAxisVO", description = "Coordinate Info About Answer Card")
public class AnswerCardAxisVO extends BaseVO {

    /**Question Node ID*/
    @ApiModelProperty(value = "Question Node ID", required = true)
    private String nodeId;

    /**Question ID*/
    @ApiModelProperty(value = "Question ID", required = true)
    private String questionId;

    /**Parent Node ID*/
    @ApiModelProperty(value = "Parent Node ID", required = true)
    private String parentNodeId;

    /**Parent Question ID*/
    @ApiModelProperty(value = "Parent Question ID", required = true)
    private String parentId;

    /**Order(The order of blank lines about a question)*/
    @ApiModelProperty(value = "The order of blank lines about a question", required = true)
    private Long sequencing;

    /**x axis Coordinate*/
    @ApiModelProperty(value = "X Axis Coordinate", required = true)
    private Double xAxis;

    /**y axis Coordinate*/
    @ApiModelProperty(value = "Y Axis Coordinate", required = true)
    private Double yAxis;

    /**The Width of Answer Area*/
    @ApiModelProperty(value = "The Width of Answer Area", required = true)
    private Double width;

    /**The Height of Answer Area*/
    @ApiModelProperty(value = "The Height of Answer Area", required = true)
    private Double height;

    /**The Page No. Of Answer Card*/
    @ApiModelProperty(value = "The Page No. Of Answer Card", required = true)
    private Long pageNo;

    /**Question Type*/
    @ApiModelProperty(value = "Question Type", required = true)
    private String typeCode;

    /**Question Type Name*/
    @ApiModelProperty(value = "Question Type Name")
    private String typeName;

    /**The Number of Question Option(Default：4)*/
    @ApiModelProperty(value = "The Number of Question Option(Default：4)")
    private Long optionCount;

    /**Number of lines About Answer area*/
    @ApiModelProperty(value = "Number of lines About Answer area")
    private Long rowCount;

    @Override
    public void validate() throws MicroServiceException {
        notBlank(nodeId, "nodeId");
        notBlank(questionId, "questionId");
        notBlank(parentNodeId, "parentNodeId");
        notBlank(parentId, "parentId");
        notNull(sequencing, "sequencing");
        notNull(xAxis, "xAxis");
        notNull(yAxis, "yAxis");
        notNull(width, "width");
        notNull(height, "height");
        notNull(pageNo, "pageNo");
        notBlank(typeCode, "typeCode");
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Long getSequencing() {
        return sequencing;
    }

    public void setSequencing(Long sequencing) {
        this.sequencing = sequencing;
    }

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

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public void setPageNo(Long pageNo) {
        this.pageNo = pageNo;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getOptionCount() {
        return optionCount;
    }

    public void setOptionCount(Long optionCount) {
        this.optionCount = optionCount;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public void setRowCount(Long rowCount) {
        this.rowCount = rowCount;
    }
}