package com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/01/28
 **/
@Data
public class AnswerRequestDataBO {

    private String answerRequestId;

    private String originData;

    // -1:处理失败 0:待处理 1:处理成功
    private Integer status;

    private Integer retryTimes;

    private String errorCode;

    private String errorMessage;

    private String creator;

    private java.util.Date createTime;

    private String updator;

    private java.util.Date updateTime;
}
