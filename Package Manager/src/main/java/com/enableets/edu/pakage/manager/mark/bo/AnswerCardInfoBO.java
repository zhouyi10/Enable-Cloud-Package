package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * 3.0答题卡信息
 * @author duffy_ding
 * @since 2019/11/06
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerCardInfoBO {

    /** 答题卡主键标识*/
    private String answerCardId;

    /** 试卷标识*/
    private String examId;

    /** 答题卡布局(1：一栏，2：两栏，3：三栏)*/
    private Integer columnType;

    /** 考号板式(1:二维码，2:准考证号，3:短考号)*/
    private Integer candidateNumberEdition;

    /** 纸张类型(A3, A4)*/
    private String pageType;

    /** 判断题 样式(0: 默认T/F, 1:对错 √/× )*/
    private String judgeStyle;

    /** 密封线(0:默认不显示， 1：显示)*/
    private String sealingLineStatus;

    /** 是否显示题干(0：默认不显示, 1:显示)*/
    private String questionContentStatus;

    /** 答题卡总页数*/
    private Integer pageCount;

    /** 答题卡状态(0:正常， 1:删除)*/
    private Integer status;

    /** 创建者*/
    private String creator;

    /** 坐标信息*/
    private List<AnswerCardAxisBO> axises;

    /**
     * 坐标信息
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AnswerCardAxisBO {

        /**主键*/
        private String axisId;

        /** 答题卡标识 */
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
}