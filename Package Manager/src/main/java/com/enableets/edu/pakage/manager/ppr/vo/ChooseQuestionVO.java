package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 *  Make Paper : Choose Question
 * @author walle_yu@enable-ets.com
 * @since 2020/08/130**/

@Data
public class ChooseQuestionVO implements java.io.Serializable{

    /** Stage Code*/
    private String stageCode;

    /** Stage Name*/
    private String stageName;

    /** Grade Code*/
    private String gradeCode;

    /** Grade Name*/
    private String gradeName;

    /** Subject Code*/
    private String subjectCode;

    /** Subject Name*/
    private String subjectName;

    /** Material Version*/
    private String materialVersion;

    /** Material Version Name*/
    private String materialVersionName;

    /** Question Type Id*/
    private String questionTypeId;

    /** Question Type Name*/
    private String questionTypeName;

    /** Question Difficulty*/
    private String questionDifficulty;

    /** Question Difficulty Name*/
    private String questionDifficultyName;

    /** 知识点检索码*/
    private String searchCode;

    /* Change question id */
    private String questionId;

    /* Knowledge point selection identification */
    private Boolean isZK;

    private String userId;

    private String params;
}
