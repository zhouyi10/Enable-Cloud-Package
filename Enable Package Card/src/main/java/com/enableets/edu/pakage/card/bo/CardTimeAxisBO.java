package com.enableets.edu.pakage.card.bo;

import lombok.Data;

/**
 * create by daniel_yin@enable-ets.com
 * at 2020/12/28 13:05
 * description: Answer card timeline information
 */
@Data
public class CardTimeAxisBO {

    /**
     * primary key
     */
    private String timelineId;

    /**
     * Answer time
     */
    private Integer triggerTime;

    /**
     * answer card primary key
     */
    private String answerCardId;

    /**
     * question node ID
     */
    private String nodeId;

    /**
     * question id
     */
    private String questionId;

    /**
     * question parent node ID
     */
    private String parentNodeId;

    /**
     * parent id
     */
    private String parentId;

    /**
     * sequencing
     */
    private Long sequencing;

    /**
     * page no
     */
    private Long pageNo;

    /**
     * Question type code
     */
    private String typeCode;

    /**
     * Question type name
     */
    private String typeName;

    /**
     * Number of options (default: 4)
     */
    private Long optionCount;

    /**
     * Number of lines
     */
    private Long rowCount;
}
