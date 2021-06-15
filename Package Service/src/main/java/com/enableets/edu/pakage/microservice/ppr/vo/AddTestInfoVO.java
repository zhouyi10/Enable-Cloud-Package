package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.BaseVO;
import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.core.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Add Test Record
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
@Data
@ApiModel(value = "AddTestInfoVO", description = "Add Test Record")
public class AddTestInfoVO extends BaseVO {

    @Override
    public void validate() throws MicroServiceException {
        this.notBlank(this.testId, "testId");
        this.notNull(this.stepId, "stepId");
        this.notBlank(this.getActivityId(), "activityId");
        this.notBlank(this.getSchoolId(), "schoolId");
        this.notBlank(this.getSchoolName(), "schoolName");
        this.notBlank(this.getTermId(), "termId");
        this.notBlank(this.getTermName(), "termName");
        this.notBlank(this.getSubjectCode(), "subjectCode");
        this.notBlank(this.getSubjectName(), "subjectName");
        this.notBlank(this.getTestName(), "testName");
        this.notNull(this.getStartTime(), "startTime");
        this.notNull(this.getEndTime(), "endTime");
        this.notBlank(this.getSender(), "sender");
        this.notBlank(this.getSenderName(), "senderName");
        this.notNull(this.getDelaySubmit(), "delaySubmit");
        this.notNull(this.getResubmit(), "resubmit");
        this.notBlank(this.getRecipients(), "recipients");
    }

    @ApiModelProperty(value = "Test Id：value Step Task of Step Send Paper Business Id")
    private String testId;

    /** Test Name*/
    @ApiModelProperty(value="Test Name", required=true)
    private String testName;

    @ApiModelProperty(value = "Step Id", required = true)
    private String stepId;

    @ApiModelProperty(value = "Value Step Task of Step Id", required = true)
    private String activityId;

    /** Activity Type*/
    @ApiModelProperty(value="Activity Type", required=true)
    private String activityType;

    /**clientId**/
    @ApiModelProperty(value="clientId", required=false)
    private String appId;

    /** Exam Document File ID*/
    @ApiModelProperty(value="Exam Document File ID", required=false)
    private String fileId;

    /** School ID*/
    @ApiModelProperty(value="School ID", required=true)
    private String schoolId;

    /** School Name*/
    @ApiModelProperty(value="School Name", required=true)
    private String schoolName;

    /** Term ID*/
    @ApiModelProperty(value="Term ID", required=true)
    private String termId;

    /** Term Name*/
    @ApiModelProperty(value="Term Name", required=true)
    private String termName;

    /** Grade Code */
    @ApiModelProperty(value="Grade Code", required=false)
    private String gradeCode;

    /** Grade Name*/
    @ApiModelProperty(value="Grade Name", required=false)
    private String gradeName;

    /** Subject Code*/
    @ApiModelProperty(value="Subject Code", required=true)
    private String subjectCode;

    /** Subject Name*/
    @ApiModelProperty(value="Subject Name", required=true)
    private String subjectName;

    /** Mark Type*/
    @ApiModelProperty(value="Mark Type", required=false)
    private String markType;

    /** Test Type*/
    @ApiModelProperty(value="Test Type", required=false)
    private String testType;

    /** Test Publish Type*/
    @ApiModelProperty(value="Test Publish Type", required=false)
    private String testPublishType;

    /** Test Start Time*/
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    @ApiModelProperty(value="Test Start Time", required=true)
    private Date startTime;

    /** Test End Time*/
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    @ApiModelProperty(value="Test End Time", required=true)
    private Date endTime;

    /** Test Sender User ID*/
    @ApiModelProperty(value="Test Sender User ID", required=true)
    private String sender;

    /** Test Sender User Name*/
    @ApiModelProperty(value="Test Sender User Name", required=true)
    private String senderName;

    /** Time allowed for late submission*/
    @ApiModelProperty(value="Time allowed for late submission", required=true)
    private Integer delaySubmit;

    /** Maximum number of submissions*/
    @ApiModelProperty(value="Maximum number of submissions", required=true)
    private Integer resubmit;

    /** Receive Student Information*/
    @ApiModelProperty(value="Receive Student Information", required=true)
    private List<TestRecipientInfoVO> recipients;
}
