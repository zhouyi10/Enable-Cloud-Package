package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/05/27
 **/
@Data
public class QuestionAssignmentTeacherMarkedProcessBO {

    private String userId;

    private String fullName;

    private Integer markedCount;

    private Float markedRatio;

    private Integer total;

}
