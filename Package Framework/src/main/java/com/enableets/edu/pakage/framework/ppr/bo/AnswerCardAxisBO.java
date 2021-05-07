package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import lombok.Data;

/**
 * Answer card coordinates
 * @author walle_yu@enable-ets.com
 * @since 2019/11/04
 **/
@Data
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
}
