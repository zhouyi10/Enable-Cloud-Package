package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.Data;

/**
 * Edit Answer Card
 * @author walle_yu@enable-ets.com
 * @since 2020/11/2
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "EditAnswerCardInfoVO", description = "Add Answer Card")
public class EditAnswerCardInfoVO extends BaseVO {

    /** Answer Card ID */
    @ApiParam(value = "Answer Card ID", required = false)
    private String answerCardId;

    /** Paper ID */
    @ApiParam(value = "Paper ID", required = true)
    private String examId;

    /** Card Layout(1：One Column，2：Two Column，3：Three Column) */
    @ApiParam(value = "Card Layout(1：One Column，2：Two Column，3：Three Column)", required = true)
    private Integer columnType;

    /** Test No. Style(1:QR code，2:Admission ticket No.，3:Short Test No.) */
    @ApiParam(value = "Test No. Style(1:QR code，2:Admission ticket No.，3:Short Test No.)", required = true)
    private Integer candidateNumberEdition;

    /** Paper type(A3, A4) */
    @ApiParam(value = "Paper type(A3, A4)")
    private String pageType;

    /** True or False Style(0: Default T/F, 1:True/False √/× ) */
    @ApiParam(value = "True or False Style(0: Default T/F, 1:True/False √/× )", required = true)
    private String judgeStyle;

    /** Sealing line(0:Default Hide， 1：Show) */
    @ApiParam(value = "Sealing line(0:Default Hide， 1：Show)", required = true)
    private String sealingLineStatus;

    /** Whether to show the stem(0：Hide(Default), 1: Show) */
    @ApiParam(value = "Whether to show the stem(0：Hide(Default), 1: Show) ", required = true)
    private String questionContentStatus;

    /** Total page Of Answer Card */
    @ApiParam(value = "Total page Of Answer Card", required = true)
    private Integer pageCount;

    /** Creator */
    @ApiModelProperty(value = "Creator")
    private String creator;

    /** Coordinate Info */
    @ApiModelProperty(value = "Axis Info", required = false)
    private List<AnswerCardAxisVO> axises;

    /** Timeline Info */
    @ApiModelProperty(value = "timeline Info", required = false)
    private List<CardTimeAxisVO> timelines;

    @Override
    public void validate() throws MicroServiceException {
        validate(answerCardId, "answerCardId");
        validate(examId, "examId");
        validate(columnType, "columnType");
        validate(candidateNumberEdition, "candidateNumberEdition");
        validate(judgeStyle, "judgeStyle");
        validate(sealingLineStatus, "sealingLineStatus");
        validate(questionContentStatus, "questionContentStatus");
        validate(pageCount, "pageCount");
    }
}
