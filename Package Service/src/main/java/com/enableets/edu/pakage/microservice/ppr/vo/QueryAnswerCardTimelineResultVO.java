package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * Answer card timeline information
 * @author gary_zhang@enable-ets.com
 * @since 2021/01/22
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "QueryAnswerCardTimelineResultVO", description = "Answer card timeline information")
public class QueryAnswerCardTimelineResultVO {

    /**Primary key*/
    @ApiModelProperty(value = "Primary key")
    private String timelineId;

    /** Answer Card ID*/
    @ApiParam(value = "Answer Card ID")
    private String answerCardId;

    /** Answer triggerTime*/
    private Integer triggerTime;

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
}
