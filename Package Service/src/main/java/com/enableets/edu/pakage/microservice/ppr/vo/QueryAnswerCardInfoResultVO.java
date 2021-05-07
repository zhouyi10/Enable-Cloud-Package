package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.core.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query answer card information VO
 * @author walle_yu@enable-ets.com
 * @since 2020/10/26
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "QueryAnswerCardInfoResultVO", description = "Query answer card information VO")
public class QueryAnswerCardInfoResultVO {

    /** Answer Card ID */
    @ApiModelProperty(value = "Answer Card ID")
    private String answerCardId;

    /** Paper ID */
    @ApiModelProperty(value = "Paper ID")
    private String examId;

    /** Card Layout(1：One Column，2：Two Column，3：Three Column) */
    @ApiModelProperty(value = "Card Layout(1：One Column，2：Two Column，3：Three Column)")
    private Integer columnType;

    /** Test No. Style(1:QR code，2:Admission ticket No.，3:Short Test No.) */
    @ApiModelProperty(value = "Test No. Style(1:QR code，2:Admission ticket No.，3:Short Test No.)")
    private Integer candidateNumberEdition;

    /** Paper type(A3, A4) */
    @ApiModelProperty(value = "Paper type(A3, A4)")
    private String pageType;

    /** True or False Style(0: Default T/F, 1:True/False √/× ) */
    @ApiModelProperty(value = "True or False Style(0: Default T/F, 1:True/False √/× )")
    private String judgeStyle;

    /** Sealing line(0:Default Hide， 1：Show)*/
    @ApiModelProperty(value = "Sealing line(0:Default Hide， 1：Show)")
    private String sealingLineStatus;

    /** Whether to show the stem(0：Hide(Default), 1: Show) */
    @ApiModelProperty(value = "Whether to show the stem(0：Hide(Default), 1: Show)")
    private String questionContentStatus;

    /** Total page Of Answer Card */
    @ApiModelProperty(value = "Total page Of Answer Card")
    private Integer pageCount;

    /** Status(0:Normal， 1:deleted) */
    @ApiModelProperty(value = "Status")
    private Integer status;

    /** Creator */
    @ApiModelProperty(value = "Creator")
    private String creator;

    /** Create Time */
    @ApiModelProperty(value = "Create Time")
    @DateTimeFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    @JsonFormat(pattern = Constants.DEFAULT_DATE_TIME_FORMAT)
    private Date createTime;

    /** Coordinate Info */
    @ApiModelProperty(value = "Coordinate Info")
    private List<QueryAnswerCardAxisResultVO> axises;

    /** Timeline Info */
    @ApiModelProperty(value = "Timeline Info")
    private List<QueryAnswerCardTimelineResultVO> timelines;
}
