package com.enableets.edu.pakage.framework.ppr.bo;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/10
 **/
@Data
public class BaseSearchConditionBO {

    private List<StageInfoBO> stages;

    private List<GradeInfoBO> grades;

    private List<SubjectInfoBO> subjects;
}
