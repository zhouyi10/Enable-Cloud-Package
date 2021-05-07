package com.enableets.edu.sdk.pakage.ppr.dto;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 **/
@Data
public class QueryAnswerDTO {

    private String testId;

    private String stepId;

    private String fileId;

    private String examId;

    private String userId;

    private String groupIds;

    private String questionIds;
}
