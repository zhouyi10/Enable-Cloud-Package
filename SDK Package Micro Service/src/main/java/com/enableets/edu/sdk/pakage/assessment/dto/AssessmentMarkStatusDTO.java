package com.enableets.edu.sdk.pakage.assessment.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 14:22
 */
@Data
public class AssessmentMarkStatusDTO {

    private String packageId;

    private String contentId;

    private String testId;

    private String testName;

    private String teacherId;

    private String teacherName;

    private List<IdNameMapDTO> studentList;

    private Date createTime;

    private Boolean isComplete;
}
