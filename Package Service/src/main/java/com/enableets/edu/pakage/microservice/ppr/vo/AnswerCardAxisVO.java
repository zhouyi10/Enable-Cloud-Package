package com.enableets.edu.pakage.microservice.ppr.vo;

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
@Data
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
        validate(nodeId, "nodeId");
        validate(questionId, "questionId");
        validate(parentNodeId, "parentNodeId");
        validate(parentId, "parentId");
        validate(sequencing, "sequencing");
        validate(xAxis, "xAxis");
        validate(yAxis, "yAxis");
        validate(width, "width");
        validate(height, "height");
        validate(pageNo, "pageNo");
        validate(typeCode, "typeCode");
    }
}