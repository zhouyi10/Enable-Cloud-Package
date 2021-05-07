package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 * Answer card timelines
 * @author gary_zhang@enable-ets.com
 * @since 2021/01/25
 **/
@Data
public class AddAnswerCardTimelineVO {

    /**Primary key*/
    private String timelineId;

    /** Answer Card ID */
    private String answerCardId;

    /** Answer triggerTime*/
    private Integer triggerTime;

    /** Question Node ID*/
    private String nodeId;

    /**Question ID*/
    private String questionId;

    /**Parent Node ID*/
    private String parentNodeId;

    /**Parent Question ID*/
    private String parentId;

    /**Order(The order of blank lines (long and empty))*/
    private Long sequencing;

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
}
