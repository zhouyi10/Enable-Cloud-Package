package com.enableets.edu.sdk.ppr.ppr.bo.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnswerCardBO {


    public AnswerCardBO(Long id, String pageType, List<CardAxisBO> axises) {
        this.answerCardId = String.valueOf(id);
        this.pageType = pageType;
        this.axises = axises;
    }

    /** 答题卡主键标识 */
    private String answerCardId;

    /** 试卷标识 */
    private String examId;

    /** 答题卡布局(1：一栏，2：两栏，3：三栏) */
    private Integer columnType;

    /** 考号板式(1:二维码，2:准考证号，3:短考号) */
    private Integer candidateNumberEdition;

    /** 纸张类型(A3, A4) */
    private String pageType;

    /** 判断题 样式(0: 默认T/F, 1:对错 √/× ) */
    private String judgeStyle;

    /** 密封线(0:默认不显示， 1：显示) */
    private String sealingLineStatus;

    /** 答题卡总页数 */
    private Integer pageCount;
    /** 创建者 */
    private String creator;

    private List<CardAxisBO> axises;

}
