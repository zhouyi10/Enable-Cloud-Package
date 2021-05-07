package com.enableets.edu.pakage.framework.actionflow.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/17 14:22
 */
@Data
public class AssessmentMarkStatusBO {

    private String status;

    private Date createTime;

    private String packageId;

    private String contentId;

    private String testId;

    private String testName;

    private String teacherId;

    private String teacherName;

    private List<IdNameMapBO> studentList;

    private String processInstanceId;

    private String currentTaskId;


}
