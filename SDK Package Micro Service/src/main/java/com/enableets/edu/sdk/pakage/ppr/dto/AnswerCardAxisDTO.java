package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Add Coordinate Info About Answer Card
 * @author walle_yu@enable-ets.com
 * @since 202/10/30
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AnswerCardAxisDTO {

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

    /**x axis Coordinate*/
    private Double xAxis;

    /**y axis Coordinate*/
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
