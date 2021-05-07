package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import lombok.Data;

/**
 * Query the review progress of the test
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryQuestionAssignmentMarkProgressResultDTO {

    private String questionId;

    private Integer markedCount;

    private Float markedRatio;

    private Integer total;

    private List<QuestionAssignmentTeacherMarkedProcessDTO> assignmentProcesses;
}
