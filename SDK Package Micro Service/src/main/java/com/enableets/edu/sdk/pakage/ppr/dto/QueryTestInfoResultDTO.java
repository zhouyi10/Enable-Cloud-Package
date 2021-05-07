package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query Test Result
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryTestInfoResultDTO {

    /** Test ID*/
    private String testId;

    /** PPR File ID*/
    private String fileId;

    /** Activity ID*/
    private String activityId;

    /** School ID*/
    private String schoolId;

    /** School Name*/
    private String schoolName;

    /** Term ID*/
    private String termId;

    /** Term Name*/
    private String termName;

    /** Grade Code*/
    private String gradeCode;

    /** Grade Name*/
    private String gradeName;

    /** Subject Code*/
    private String subjectCode;

    /** Subject Name*/
    private String subjectName;

    /** Test Name*/
    private String testName;

    /** PPR exam ID*/
    private String examId;

    /** Exam Name*/
    private String examName;

    /** Exam Type*/
    private String examType;

    /** Exam Total Score*/
    private Float score;

    /** Test Start Time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** Test End Time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Test Time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date testTime;

    /** Start Submit Answer Card Time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startSubmitTime;

    /** End Submit Answer Card Time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endSubmitTime;

    /** Publish Test User ID*/
    private String sender;

    /** Publish Test User Name*/
    private String senderName;

    /** Test Cost Time*/
    private Float testCostTime;

    /** Time allowed for late submission*/
    private Integer delaySubmit;

    /** Allow repeated submissions*/
    private Integer resubmit;

    /** Creator*/
    private String creator;

    /** Creation time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** Updater*/
    private String updator;

    /** Update time*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**Test Recipient details*/
    private List<TestRecipientInfoDTO> recipients;
}
