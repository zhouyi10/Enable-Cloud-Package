package com.enableets.edu.pakage.framework.ppr.test.service.submit.bo;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/07
 **/
@Data
public class PPRAnswerInfoBO {

    private String questionId;

    private Long parentId;

    private String answer;

    private List<CanvasBO> canvases;

    private List<AnswerTrailBO> trails;

    private Float answerScore;

    private String answerStatus;

    private String markStatus;
}
