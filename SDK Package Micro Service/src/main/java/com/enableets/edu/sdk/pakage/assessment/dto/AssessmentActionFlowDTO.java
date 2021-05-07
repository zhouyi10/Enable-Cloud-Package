package com.enableets.edu.sdk.pakage.assessment.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 15:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AssessmentActionFlowDTO {

    private String packageId;

    private String contentId;

    private String testId;

    private String testName;

    private String teacherId;

    private String teacherName;

    private List<IdNameMapDTO> studentList;
}
