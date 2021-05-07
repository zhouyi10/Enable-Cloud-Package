package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * Review progress
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "QueryQuestionAssignmentMarkProgressResultVO", description = "Review progress")
public class QueryQuestionAssignmentMarkProgressResultVO {

    @ApiModelProperty(value = "Question ID")
    private String questionId;

    @ApiModelProperty(value = "Number of people reviewed")
    private Integer markedCount;

    @ApiModelProperty(value = "Review progress")
    private Float markedRatio;

    @ApiModelProperty(value = "Total people")
    private Integer total;

    @ApiModelProperty(value = "Teacher review progress")
    private List<QuestionAssignmentTeacherMarkedProcessVO> assignmentProcesses;
}
