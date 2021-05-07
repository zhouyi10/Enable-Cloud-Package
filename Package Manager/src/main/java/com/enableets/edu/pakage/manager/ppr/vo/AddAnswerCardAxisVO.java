package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 * Answer card coordinates
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
@Data
public class AddAnswerCardAxisVO {

    /**Primary key*/
    private String axisId;

    /** Answer Card ID */
    private String answerCardId;

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
}
