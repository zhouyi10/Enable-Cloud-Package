package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Answer card coordinate information
 * @author walle_yu@enable-ets.com
 * @since 2020/11/22
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryAnswerCardAxisResultDTO {

    /**Primary key*/
    private String axisId;

    /** Answer Card ID*/
    private String answerCardId;

    /**Question Node ID*/
    private String nodeId;

    /**Question ID*/
    private String questionId;

    /**Parent Node ID*/
    private String parentNodeId;

    /**Parent Question ID*/
    private String parentId;

    /**Order(The order of blank lines about a question)*/
    private Long sequencing;

    /**X Axis Coordinate*/
    private Double xAxis;

    /**Y Axis Coordinate*/
    private Double yAxis;

    /**The Width of Answer Area*/
    private Double width;

    /**The Height of Answer Area*/
    private Double height;

    /**The Page No. Of Answer Card*/
    private Long pageNo;

    /**Question Type*/
    private String typeCode;

    /**Question Type Name*/
    private String typeName;

    /**The Number of Question Option(Default：4)*/
    private Long optionCount;

    /**Number of lines About Answer area*/
    private Long rowCount;
}
