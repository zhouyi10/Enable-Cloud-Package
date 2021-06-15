package com.enableets.edu.pakage.framework.ppr.bo.message.paper.send;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/17
 **/
@Data
@Accessors(chain = true)
public class StepInfoBO {

    private String stepId;

    private String type;

    private String businessId;

    private String name;

    private Date publishTime;

    private Date startTime;

    private Date endTime;

    private String description;

    private Integer sequence;

    private String subjectCode;

    private String subjectName;

    private String gradeCode;

    private String gradeName;

    private String courseId;

    private String courseName;

    private Float totalScore;

    private List<ContentInfoBO> contents;

    private List<GroupInfoBO> groups;
}
