package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * Answer card coordinate information
 * @author walle_yu@enable-ets.com
 * @since 2020/10/26
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "QueryAnswerCardAxisResultVO", description = "Answer card coordinate information")
public class QueryAnswerCardAxisResultVO {

    /**Primary key*/
    @ApiModelProperty(value = "Primary key")
    private String axisId;

    /** Answer Card ID*/
    @ApiParam(value = "Answer Card ID")
    private String answerCardId;

    /**Question Node ID*/
    @ApiModelProperty(value = "Question Node ID")
    private String nodeId;

    /**Question ID*/
    @ApiModelProperty(value = "Question ID")
    private String questionId;

    /**Parent Node ID*/
    @ApiModelProperty(value = "Parent Node ID")
    private String parentNodeId;

    /**Parent Question ID*/
    @ApiModelProperty(value = "Parent Question ID")
    private String parentId;

    /**Order(The order of blank lines about a question)*/
    @ApiModelProperty(value = "Order(The order of blank lines about a question)")
    private Long sequencing;

    /**X Axis Coordinate*/
    @ApiModelProperty(value = "X Axis Coordinate")
    private Double xAxis;

    /**Y Axis Coordinate*/
    @ApiModelProperty(value = "Y Axis Coordinate")
    private Double yAxis;

    /**The Width of Answer Area*/
    @ApiModelProperty(value = "The Width of Answer Area")
    private Double width;

    /**The Height of Answer Area*/
    @ApiModelProperty(value = "The Height of Answer Area")
    private Double height;

    /**The Page No. Of Answer Card*/
    @ApiModelProperty(value = "The Page No. Of Answer Card")
    private Long pageNo;

    /**Question Type*/
    @ApiModelProperty(value = "Question Type")
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
