package com.enableets.edu.sdk.pakage.ppr.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * Query answer card information DTO
 * @author walle_yu@enable-ets.com
 * @since 2020/11/2
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryAnswerCardInfoResultDTO {

    /** Answer Card ID */
    private String answerCardId;

    /** Paper ID */
    private String examId;

    /** Card Layout(1：One Column，2：Two Column，3：Three Column) */
    private Integer columnType;

    /** Test No. Style(1:QR code，2:Admission ticket No.，3:Short Test No.) */
    private Integer candidateNumberEdition;

    /** Paper type(A3, A4) */
    private String pageType;

    /** True or False Style(0: Default T/F, 1:True/False √/× ) */
    private String judgeStyle;

    /** Sealing line(0:Default Hide， 1：Show)*/
    private String sealingLineStatus;

    /** Whether to show the stem(0：Hide(Default), 1: Show) */
    private String questionContentStatus;

    /** Total page Of Answer Card */
    private Integer pageCount;

    /** Status(0:Normal， 1:deleted) */
    private Integer status;

    /** Creator */
    private String creator;

    /** Create Time */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** Coordinate Info */
    private List<QueryAnswerCardAxisResultDTO> axises;

    /** Timeline info */
    private List<QueryCardTimelineAxisResultDTO> timelines;
}
