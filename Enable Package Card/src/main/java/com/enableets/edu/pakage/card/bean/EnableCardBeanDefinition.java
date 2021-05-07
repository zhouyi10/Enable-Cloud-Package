package com.enableets.edu.pakage.card.bean;

import com.enableets.edu.pakage.card.bo.CardAxisBO;
import com.enableets.edu.pakage.card.bo.CardTimeAxisBO;
import com.enableets.edu.pakage.card.bo.action.StepActionBO;
import com.enableets.edu.pakage.card.bo.answer.AnswerBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * EnableCard
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnableCardBeanDefinition {

    /** Primary key*/
    private String answerCardId;

    /** Paper ID */
    private String examId;

    /** Answer sheet layout(1：One column，2：Two columns，3：Three columns) */
    private Integer columnType;

    /** 考号板式(1:二维码，2:准考证号，3:短考号) */
    private Integer candidateNumberEdition;

    /** 纸张类型(A3, A4) */
    private String pageType;

    /** 答题卡总页数 */
    private Integer pageCount;

    private List<CardAxisBO> axises;

    private List<CardTimeAxisBO> timelines;

    private List<AnswerBO> answers;

    private List<StepActionBO> actions;
}
