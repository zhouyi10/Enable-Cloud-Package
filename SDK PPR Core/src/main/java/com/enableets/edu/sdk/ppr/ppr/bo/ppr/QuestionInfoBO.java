package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.enableets.edu.sdk.ppr.ppr.bo.CodeNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Question
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionInfoBO {

    private String questionId;

    private CodeNameMapBO type;

    private CodeNameMapBO difficulty;

    private QuestionStemInfoBO stem;

    private QuestionAnswerInfoBO answer;

    private String affixId;

    private Float points;

    private Float estimateTime;

    /** question option*/
    private List<QuestionOptionInfoBO> options;

    private List<KnowledgeInfoBO> knowledges;

    private List<QuestionAxisBO> axises;
}
