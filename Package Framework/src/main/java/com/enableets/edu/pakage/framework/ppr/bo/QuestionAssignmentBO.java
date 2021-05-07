package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class QuestionAssignmentBO {

    /**主键标识 */
    private String assignmentId;

    /** 测验标识*/
    private String testId;

    /**题目标识（大题） */
    private String questionId;

    /**用户标识 */
    private String userId;

    /*老师名称*/
    private String fullName;

    /**序号 */
    private Integer sequence;

    /**创建者*/
    private String creator;

    /**创建时间*/
    private Date createTime;

    /**更新者*/
    private String updator;

    /**更新时间*/
    private Date updateTime;

    /** 已提交人数*/
    private Integer submitCount = 0;

    /** 已批阅人数*/
    private Integer markedCount = 0;

    /** 分配的批阅学生*/
    private List<QuestionAssignmentRecipientBO> recipients;
}
