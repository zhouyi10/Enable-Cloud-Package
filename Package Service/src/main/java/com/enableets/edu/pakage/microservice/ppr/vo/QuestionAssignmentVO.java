package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "questionAssignmentVO", description = "question assignment info")
public class QuestionAssignmentVO extends BaseVO {

    /** Test ID*/
    @ApiModelProperty(value = "Test ID")
    private String testId;

    /**Question ID（Outer Question ID） */
    @ApiModelProperty(value = "Question ID")
    private String questionId;

    /**User ID */
    @ApiModelProperty(value = "User ID")
    private String userId;

    /**User Name */
    @ApiModelProperty(value = "User Name")
    private String fullName;

    /**Order No. */
    @ApiModelProperty(value = "Order No.")
    private String sequence;


    @Override
    public void validate() throws MicroServiceException {
        this.validate(this.getUserId(), "userId");
        this.validate(this.getTestId(), "testId");
        this.validate(this.getQuestionId(), "questionId");
    }
}
