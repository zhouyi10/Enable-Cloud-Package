package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/18
 **/
@Data
public class QueryMarkAnswerBO {

    private String testId;

    private String stepId;

    private String examId;

    private String userId;

    private String groupIds;

    private String questionIds;

    private String fileId;
}
