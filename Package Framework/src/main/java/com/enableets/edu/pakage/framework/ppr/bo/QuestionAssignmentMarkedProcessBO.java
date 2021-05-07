package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/24
 **/
@Data
public class QuestionAssignmentMarkedProcessBO {

    /** 题目标识*/
    private String questionId;

    /** 已批阅人数*/
    private Integer markedCount = 0;

    /** 批阅进度比率*/
    private Float markedRatio;

    /** 总人数*/
    private Integer total = 0;

    /** 指派对象批阅进度*/
    private List<QuestionAssignmentTeacherMarkedProcessBO> assignmentProcesses;
}
