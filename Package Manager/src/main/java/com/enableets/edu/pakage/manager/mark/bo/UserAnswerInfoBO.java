package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/19
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAnswerInfoBO {

    private String questionId;

    private String parentId;

    private String userId;

    private String userName;

    private String answerId;

    private String testUserId;

    private String userAnswer;

    private String answerScore;

    private String answerStatus;

    private String markStatus;

    private Float questionScore;

    private List<AnswerCanvasBO> canvases;

}
