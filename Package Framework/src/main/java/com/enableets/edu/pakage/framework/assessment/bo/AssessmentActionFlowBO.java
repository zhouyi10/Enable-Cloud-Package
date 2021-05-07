package com.enableets.edu.pakage.framework.assessment.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 15:11
 */
@Data
public class AssessmentActionFlowBO {

    private String packageId;

    private String contentId;

    private String testId;

    private String testName;

    private String teacherId;

    private String teacherName;

    private List<IdNameMapBO> studentList;

    private String processInstanceId;

    private String currentTaskId;

    private Boolean isComplete;

    private Date createTime;
}
