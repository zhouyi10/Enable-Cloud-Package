package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import lombok.Data;

/**
 * Answer card coordinates
 * @author walle_yu@enable-ets.com
 * @since 2019/11/04
 **/
public class AnswerCardAxisBO {

    /**Primary key*/
    private Long axisId;

    /** Answer Card ID */
    private Long answerCardId;

    /** Question Node ID*/
    private Long nodeId;

    /**Question ID*/
    private Long questionId;

    /**Parent Node ID*/
    private Long parentNodeId;

    /**Parent Question ID*/
    private Long parentId;

    /**Order(The order of blank lines (long and empty))*/
    private Long sequencing;

    /**x Axis*/
    private Double xAxis;

    /**y Axis*/
    private Double yAxis;

    /**width*/
    private Double width;

    /**height*/
    private Double height;

    /**Page No*/
    private Long pageNo;

    /**Type Code*/
    private String typeCode;

    /**Type Name*/
    private String typeName;

    /**Option Count(default ：4)*/
    private Long optionCount;

    /**Row Count*/
    private Long rowCount;

    /**Creator*/
    private String creator;

    /**Create Time*/
    private Date createTime;

    /**updator*/
    private String updator;

    /**Update Time*/
    private Date updateTime;

    public Long getAxisId() {
        return axisId;
    }

    public void setAxisId(Long axisId) {
        this.axisId = axisId;
    }

    public Long getAnswerCardId() {
        return answerCardId;
    }

    public void setAnswerCardId(Long answerCardId) {
        this.answerCardId = answerCardId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(Long parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
