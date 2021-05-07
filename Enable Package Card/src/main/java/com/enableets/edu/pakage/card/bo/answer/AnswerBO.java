package com.enableets.edu.pakage.card.bo.answer;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/20
 **/
@Data
public class AnswerBO {

    private Long questionId;

    private Long parentId;

    private String answer;

    private List<CanvasBO> canvases;

    private List<AnswerTrailBO> trails;
}
