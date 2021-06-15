package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: gary_zhang@enable-ets.com
 * @Date: 2021/1/15 0:16
 * @Description: Answer card timeline information
 */
@Data
public class CardTimeAxisBO {
    /**
     * primary key
     */
    private Long timelineId;

    /**
     * Answer time
     */
    private Integer triggerTime;

    /**
     * answer card primary key
     */
    private Long answerCardId;

    /**
     * question node ID
     */
    private Long nodeId;

    /**
     * question id
     */
    private Long questionId;

    /**
     * question parent node ID
     */
    private Long parentNodeId;

    /**
     * parent id
     */
    private Long parentId;

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

    /**
     * creator
     */
    private String creator;

    /**
     * create time
     */
    private Date createTime;

    /**
     * updator
     */
    private String updator;

    /**
     * update_time
     */
    private Date updateTime;
}
