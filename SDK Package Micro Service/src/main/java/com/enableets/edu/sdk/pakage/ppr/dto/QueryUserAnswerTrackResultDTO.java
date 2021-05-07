package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import lombok.Data;

/**
 * User track information
 * @author duffy_ding
 * @since 2020/10/19
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryUserAnswerTrackResultDTO {

    /** Primary key ID */
    private String answerStampId;

    /** Answer ID */
    private String answerId;

    /** Starting time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginTime;

    /** End Time */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** Answer Last time */
    private Long lastTime;

    /** Question ID */
    private String questionId;

    /** Question No. */
    private String questionNo;

    /** Test ID */
    private String testId;

    /** Exam ID */
    private String examId;

    /** Answer User ID */
    private String creator;

}
