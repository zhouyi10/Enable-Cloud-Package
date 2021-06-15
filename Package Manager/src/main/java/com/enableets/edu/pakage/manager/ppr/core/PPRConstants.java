package com.enableets.edu.pakage.manager.ppr.core;

/**
 * PPR Static Variable
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
public class PPRConstants {

    /** */
    public static final String QUESTION_ANSWER_CONTENT_REPLACE_REGEX = "[　\\s]*<[^<>]*>[　\\s]*(答案|解析|听力原文)[　\\s]*</\\w*>[　\\s]*";


    /** Activity Assessment Step: receive activity paper*/
    public static final String STEP_ACTION_RECEIVE_ACTIVITY_PAPER_TYPE = "2";

    /** Activity Assessment Step: answer paper type*/
    public static final String STEP_ACTION_ANSWER_PAPER_TYPE = "3";

    /** Activity Assessment Step: submit paper type*/
    public static final String STEP_ACTION_SUBMIT_PAPER_TYPE = "4";

    /** Activity Assessment Step: receive answer paper type*/
    public static final String STEP_ACTION_RECEIVE_ANSWER_TYPE = "5";

    /** session key Teacher Grade Grade Grade Information */
    public static final String SESSION_KEY_TEACHER_BASE_INFO = "_TEACHER_SGS_BASE_INFO";

    public static final String PACKAGE_PPR_CACHE_KEY_PREFIX = "com:enableets:edu:package:ppr:";

    /** 字典等级 第一级 */
    public static final String DICTIONARY_MENU_LEVEL_ROOT = "0";

    /** 题型字典类型 **/
    public static final String DICTIONARY_QUESTONTYPE_TYPE_CODE = "11";

    /** 默认学校id **/
    public static final String DEFAULT_SCHOOL_ID = "999999";

    /** 框题试卷试卷类型*/
    public static final String PPR_BOX_QUESTION_PAPER_TYPE = "4";

    /** 微课试卷*/
    public static final String PPR_MICRO_COURSE_TIME_SHARING_PAPER = "5";

    /** 文件试卷试卷类型*/
    public static final String PPR_FILE_QUESTION_PAPER_TYPE = "1";
}
