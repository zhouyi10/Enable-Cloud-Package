package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/17
 **/
@Data
public class QueryQuestionBO {

    private String stageCode;

    private String gradeCode;

    private String subjectCode;

    /** 题型*/
    private String typeCode;

    private String abilityCode;

    private String difficultyCode;

    private String schoolId;

    private String keyword;

    private String searchCode;

    private String materialVersion;

    private String questionNo;

    private String providerCode;

    private String creator;

    private Integer offset;

    private Integer rows;

    private String zoneCode;
}
