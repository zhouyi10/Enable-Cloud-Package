package com.enableets.edu.pakage.card.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * 答题卡坐标信息
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CardAxisBO {

    /**主键*/
    private String axisId;

    /** 答题卡主键标识*/
    private String answerCardId;

    /**题目节点标识*/
    private String nodeId;

    /**题目标识*/
    private String questionId;

    /**题目父节点标识*/
    private String parentNodeId;

    /**父题目标识*/
    private String parentId;

    /**顺序(题目空行(多空)的顺序)*/
    private Long sequencing;

    /**x轴坐标*/
    private Double xAxis;

    /**y轴坐标*/
    private Double yAxis;

    /**宽度*/
    private Double width;

    /**高度*/
    private Double height;

    /**答题卡页码*/
    private Long pageNo;

    /**题型*/
    private String typeCode;

    /**题型名称*/
    private String typeName;

    /**选项个数(默认：4)*/
    private Long optionCount;

    /**行数*/
    private Long rowCount;
}
