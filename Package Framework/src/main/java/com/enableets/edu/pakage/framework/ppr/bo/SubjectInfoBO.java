package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectInfoBO {

    private String subjectCode;

    private String subjectName;

    private String gradeCode;
}
