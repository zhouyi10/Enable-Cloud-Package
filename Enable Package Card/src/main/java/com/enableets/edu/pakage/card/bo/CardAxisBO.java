package com.enableets.edu.pakage.card.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 答题卡坐标信息
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class CardAxisBO {

    /**主键*/
    private String axisId;

    /** 答题卡主键标识*/
    private String answerCardId;

    /**题目节点标识*/
    private String nodeId;

    /**题目标识*/
    private String questionId;

    /**题目父节点标识*/
    private String parentNodeId;

    /**父题目标识*/
    private String parentId;

    /**顺序(题目空行(多空)的顺序)*/
    private Long sequencing;

    /**x轴坐标*/
    private Double xAxis;

    /**y轴坐标*/
    private Double yAxis;

    /**宽度*/
    private Double width;

    /**高度*/
    private Double height;

    /**答题卡页码*/
    private Long pageNo;

    /**题型*/
    private String typeCode;

    /**题型名称*/
    private String typeName;

    /**选项个数(默认：4)*/
    private Long optionCount;

    /**行数*/
    private Long rowCount;

    public String getAxisId() {
        return axisId;
    }

    public void setAxisId(String axisId) {
        this.axisId = axisId;
    }

    public String getAnswerCardId() {
        return answerCardId;
    }

    public void setAnswerCardId(String answerCardId) {
        this.answerCardId = answerCardId;
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
