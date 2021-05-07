package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.core.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import lombok.Data;

/**
 * User Answer Stamp
 * @author duffy_ding
 * @since 2020/10/19
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueryUserAnswerTrackResultVO {

    /** Key */
    private String answerStampId;

    /** Answer ID */
    private String answerId;

    /** Start Time */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    private Date beginTime;

    /** End Time */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT, timezone = "GMT+8")
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    private Date endTime;

    /** Answer Last Time */
    private Long lastTime;

    /** Question ID */
    private String questionId;

    /** Question No */
    private String questionNo;

    /** Test ID */
    private String testId;

    /** Exam ID */
    private String examId;

    /** Answer User ID */
    private String creator;
}
