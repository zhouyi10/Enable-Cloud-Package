package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Save Question Assign Parameters
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAssignmentDTO{

    /** Test ID*/
    private String testId;

    /**Question ID(Outer Question) */
    private String questionId;

    /**User ID */
    private String userId;

    /**User Full Name(Teacher) */
    private String fullName;

    /**Serial number */
    private String sequence;

}
