package com.enableets.edu.pakage.framework.etm.bo;

import com.enableets.edu.pakage.framework.ppr.bo.GradeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.StageInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.SubjectInfoBO;
import lombok.Data;

import java.util.List;


@Data
public class BaseSearchConditionBO {

    private List<StageInfoBO> stages;

    private List<GradeInfoBO> grades;

    private List<SubjectInfoBO> subjects;
}
