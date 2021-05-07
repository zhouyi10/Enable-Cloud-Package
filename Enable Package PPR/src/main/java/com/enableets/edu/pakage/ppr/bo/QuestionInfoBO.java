package com.enableets.edu.pakage.ppr.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionInfoBO {

    private String questionId;

    private String parentId;

    private CodeNameMapBO type;

    private CodeNameMapBO stage;

    private CodeNameMapBO grade;

    private CodeNameMapBO subject;

    private CodeNameMapBO ability;

    private CodeNameMapBO difficulty;

    private Float estimateTime;

    private Float points;

    private QuestionStemInfoBO stem;

    private QuestionAnswerInfoBO answer;

    private String listen;

    private String affixId;

    private String questionNo;

    private List<QuestionOptionInfoBO> options;

    private List<QuestionAxisInfoBO> axises;

    private List<QuestionKnowledgeInfoBO> knowledges;
}
