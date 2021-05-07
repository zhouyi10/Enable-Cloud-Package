package com.enableets.edu.sdk.ppr.ppr.bo.card.answer;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/30
 **/
@Data
public class AnswerBO {

    public AnswerBO(){}

    public AnswerBO(Long questionId, Long parentId, String answer){
        this.questionId = questionId;
        this.parentId = parentId;
        this.answer = answer;
    }

    public AnswerBO(Long questionId, Long parentId, String answer, List<AnswerTrailBO> trails){
        this.questionId = questionId;
        this.parentId = parentId;
        this.answer = answer;
        this.trails = trails;
    }

    private Long questionId;

    private Long parentId;

    private String answer;

    private List<CanvasBO> canvases;

    private List<AnswerTrailBO> trails;
}
