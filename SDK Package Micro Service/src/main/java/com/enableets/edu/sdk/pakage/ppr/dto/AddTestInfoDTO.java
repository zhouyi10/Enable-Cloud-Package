package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * New Add Test Info
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddTestInfoDTO {

    private String activityId;

    /** 活动类型*/
    private String activityType;

    /**clientId**/
    private String appId;

    /** 试卷标识*/
    private String examId;

    /** 试卷标识*/
    private String examName;

    /** 试卷.paper文件标识*/
    private String fileId;

    /** 学校标识*/
    private String schoolId;

    /** 学校名称*/
    private String schoolName;

    /** 学期标识*/
    private String termId;

    /** 学期名称*/
    private String termName;

    /** 年级编码 */
    private String gradeCode;

    /** 年级名称*/
    private String gradeName;

    /** 科目编码*/
    private String subjectCode;

    /** 科目名称*/
    private String subjectName;

    /** 考试名称*/
    private String testName;

    /** 批阅类型*/
    private String markType;

    /** 考试类型*/
    private String testType;

    /** 发布类型*/
    private String testPublishType;

    /** 考试开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** 考试结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 考试派发用户标识*/
    private String sender;

    /** 考试派发用户名称*/
    private String senderName;

    /** 考试时长*/
    private Integer testCostTime;

    /** 允许迟交时长*/
    private Integer delaySubmit;

    /** 交卷次数*/
    private Integer resubmit;

    /** 派卷接收对象信息*/
    private List<TestRecipientInfoDTO> recipients;
}
