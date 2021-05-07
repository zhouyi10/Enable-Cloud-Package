package com.enableets.edu.pakage.manager.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * Student Answer Info
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/19
 */
@Data
public class MarkActionInfoBO {

    /** 考试标识*/
    private String testId;

    /** 考试标识*/
    private String examId;

    /** 考试名称*/
    private String testName;

    /** 交卷人数*/
    private Integer submitCount;

    /** 批阅状态*/
    private String markStatus;

    /** 0：暂存， 1：批阅*/
    private Integer type;

    /** 按学生分类*/
    private List<UserAnswerInfoBO> answers;

    /** 总人数*/
    private Integer totalCount;

    private String userId;

    private String questionId;

}
