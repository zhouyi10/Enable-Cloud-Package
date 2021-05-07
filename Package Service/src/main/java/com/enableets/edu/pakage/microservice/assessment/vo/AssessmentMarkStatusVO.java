package com.enableets.edu.pakage.microservice.assessment.vo;

import com.enableets.edu.pakage.framework.actionflow.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 14:22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AssessmentMarkStatusVO", description = "Assessment Mark Status")
public class AssessmentMarkStatusVO {

    @ApiParam(value = "Package Id")
    private String packageId;

    @ApiParam(value = "Content Id")
    private String contentId;

    @ApiParam(value = "Test Id")
    private String testId;

    @ApiParam(value = "Test Name")
    private String testName;

    @ApiParam(value = "Teacher Id")
    private String teacherId;

    @ApiParam(value = "Teacher Name")
    private String teacherName;

    @ApiParam(value = "Student List")
    private List<IdNameMapVO> studentList;

    @ApiParam(value = "Create Time")
    private Date createTime;

    @ApiParam(value = "Is Complete")
    private Boolean isComplete;
}
