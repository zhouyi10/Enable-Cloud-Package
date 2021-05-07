package com.enableets.edu.pakage.microservice.actionflow.vo;

import com.enableets.edu.pakage.framework.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 15:11
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "QueryAssessmentActionFlowVO", description = "Query Assessment Action Flow")
public class QueryAssessmentActionFlowVO {

    @ApiParam(value = "Package Id", required = true)
    private String packageId;

    @ApiParam(value = "Content Id", required = true)
    private String contentId;

    @ApiParam(value = "Test Id", required = true)
    private String testId;

    @ApiParam(value = "Test Name", required = false)
    private String testName;

    @ApiParam(value = "Teacher Id", required = true)
    private String teacherId;

    @ApiParam(value = "Teacher Name", required = false)
    private String teacherName;

    @ApiParam(value = "Students info", required = true)
    private List<IdNameMapBO> studentList;

    @ApiParam(value = "Process Instance Id", required = false)
    private String processInstanceId;

    @ApiParam(value = "Current Task Id", required = false)
    private String currentTaskId;

}
