package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query the teacher's assigned review information
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryQuestionAssignmentResultDTO {

    /**Assign ID */
    private String assignmentId;

    /** Test ID*/
    private String testId;

    /**Question ID */
    private String questionId;

    /**User ID */
    private String userId;

    /*User Full Name*/
    private String fullName;

    /** Sequence */
    private String sequence;

    /**Creator*/
    private String creator;

    /**Create Time*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**Updater*/
    private String updator;

    /**Update Time*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** Mark Recipient*/
    private List<QueryQuestionAssignmentRecipientResultDTO> recipients;
}
