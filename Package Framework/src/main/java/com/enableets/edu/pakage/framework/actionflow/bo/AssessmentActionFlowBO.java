package com.enableets.edu.pakage.framework.actionflow.bo;

import com.enableets.edu.pakage.framework.actionflow.bo.IdNameMapBO;
import lombok.Data;

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
}
