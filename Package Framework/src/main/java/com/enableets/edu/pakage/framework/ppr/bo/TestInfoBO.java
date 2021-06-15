package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/
@Data
@Accessors(chain = true)
public class TestInfoBO {

    private String testId;

    private String stepId;

    private String activityId;

    private String activityType;

    private String schoolId;

    private String termId;

    private String gradeCode;

    private String gradeName;

    private String subjectCode;

    private String subjectName;

    private String testName;

    private String examId;

    private String fileId;

    private String markType;

    private String testType;

    private String testPublishType;

    private java.util.Date testPublishTime;

    private java.util.Date testTime;

    private java.util.Date startTime;

    private java.util.Date endTime;

    private java.util.Date startSubmitTime;

    private java.util.Date endSubmitTime;

    private String sender;

    private String senderName;

    private String creator;

    private java.util.Date createTime;

    private String updator;

    private java.util.Date updateTime;

    private String delStatus;

    private Float testCostTime;

    private String appId;

    private Integer delaySubmit;

    private Integer resubmit;

    private String from;

    private String examName;

    private List<TestRecipientInfoBO> recipients;

    private String processInstanceId;
}
