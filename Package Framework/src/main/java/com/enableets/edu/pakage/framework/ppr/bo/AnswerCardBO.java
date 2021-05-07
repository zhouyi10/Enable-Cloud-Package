package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/23
 **/
@Data
public class AnswerCardBO {

    private String testId;

    private String paperId;

    private String stepId;

    private String userId;

    private String startTime;

    private String endTime;

    private List<AnswerInfoBO> answers;

    @Data
    public static class AnswerInfoBO{

        private String questionId;

        private String parentId;

        private String userAnswer;

        private List<String> answerStamp;

        private List<AnswerCanvas> canvases;

        private String answerCostTime;
    }

    @Data
    public static class AnswerCanvas{

        private String fileId;

        private String fileName;

        private String fileExt;

        private String url;

        private String md5;

        private Integer order;

    }
}
