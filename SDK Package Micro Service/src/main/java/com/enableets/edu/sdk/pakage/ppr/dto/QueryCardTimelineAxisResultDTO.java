package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author: gary_zhang@enable-ets.com
 * @Date: 2021/1/22 13:48
 * @Description: Answer card timeline information
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryCardTimelineAxisResultDTO {
    /**Primary key*/
    private String timelineId;

    /** Answer Card ID*/
    private String answerCardId;

    /** Answer triggerTime*/
    private Integer triggerTime;

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
