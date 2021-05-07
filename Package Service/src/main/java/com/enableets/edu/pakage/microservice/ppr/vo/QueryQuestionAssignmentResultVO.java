package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query the teacher's assigned marking information
 */
@Data
@ApiModel(value = "QueryQuestionAssignmentResultVO", description = "Query the teacher's assigned marking information")
public class QueryQuestionAssignmentResultVO {

    /**ID */
    @ApiModelProperty(value = "ID")
    private String assignmentId;

    /** Test ID*/
    @ApiModelProperty(value = "Test ID")
    private String testId;

    /**Question ID（Outer question） */
    @ApiModelProperty(value = "Question ID")
    private String questionId;

    /**User ID */
    @ApiModelProperty(value = "User ID")
    private String userId;

    /*Full Name*/
    @ApiModelProperty(value = "Full Name")
    private String fullName;

    /**Order No. */
    @ApiModelProperty(value = "Order No.")
    private String sequence;

    /**Creator*/
    @ApiModelProperty(value = "Creator")
    private String creator;

    /**Create Time*/
    @ApiModelProperty(value = "Create Time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**Updater*/
    @ApiModelProperty(value = "Updater")
    private String updator;

    /**Update Time*/
    @ApiModelProperty(value = "Update Time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** Marking Students*/
    @ApiModelProperty(value = "Marking Students")
    private List<QueryQuestionAssignmentRecipientResultVO> recipients;
}
