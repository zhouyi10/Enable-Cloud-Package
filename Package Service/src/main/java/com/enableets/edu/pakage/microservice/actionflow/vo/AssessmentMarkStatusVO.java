package com.enableets.edu.pakage.microservice.actionflow.vo;

import com.enableets.edu.pakage.framework.actionflow.bo.IdNameMapBO;
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
@ApiModel(value = "AssessmentMarkStatusVO", description = "Assessment Mark Status")
public class AssessmentMarkStatusVO {

    @ApiParam(value = "Status", required = false)
    private String status;

    @ApiParam(value = "Create Time", required = false)
    private Date createTime;

    @ApiParam(value = "Package Id", required = false)
    private String packageId;

    @ApiParam(value = "Content Id", required = false)
    private String contentId;

    @ApiParam(value = "Test Id", required = false)
    private String testId;

    @ApiParam(value = "Test Name", required = false)
    private String testName;

    @ApiParam(value = "Teacher Id", required = false)
    private String teacherId;

    @ApiParam(value = "Teacher Name", required = false)
    private String teacherName;

    @ApiParam(value = "Student List", required = false)
    private List<IdNameMapBO> studentList;

    @ApiParam(value = "Process Instance Id", required = false)
    private String processInstanceId;

    @ApiParam(value = "Current Task Id", required = false)
    private String currentTaskId;
}
