package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Review by question-Teacher review progress
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAssignmentTeacherMarkedProcessDTO {

    /** User ID*/
    private String userId;

    /** User Full Name*/
    private String fullName;

    /** Number of reviews*/
    private Integer markedCount;

    /** Number of reviews Percentage*/
    private Float markedRatio;

    /** Total*/
    private Integer total;

}
