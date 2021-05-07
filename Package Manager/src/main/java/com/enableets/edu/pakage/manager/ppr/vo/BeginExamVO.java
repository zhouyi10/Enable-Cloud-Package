package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/

@Data
public class BeginExamVO {

    private String testId;

    private String stepId;

    private String fileId;

    private String examId;

    private String userId;

    private boolean isMobile = Boolean.FALSE;
}
