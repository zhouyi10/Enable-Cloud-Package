package com.enableets.edu.pakage.manager.ppr.core;

/**
 * PPR Static Variable
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
public class PPRConstants {

    public static final String CLIENT_APP = "ppr";

    /** Paper Kind Level  */
    public static final int LEVEL_EXAM_KIND = 1;

    /** Paper Question Type Level  */
    public static final int LEVEL_EXAM_QUES_TYPE = 2;

    /** Paper Question Level  */
    public static final int LEVEL_EXAM_QUES = 3;

    /** Paper Question Child Level  */
    public static final int LEVEL_EXAM_QUES_CHILD = 4;

    /** */
    public static final String QUESTION_ANSWER_CONTENT_REPLACE_REGEX = "[　\\s]*<[^<>]*>[　\\s]*(答案|解析|听力原文)[　\\s]*</\\w*>[　\\s]*";

    /**Cache Key Prefix: test */
    public static final String TEST_CACHE_KEY_PREFIX = "com:enableets:edu:package:ppr:test:";

    /**Cache Key Prefix: test recipient*/
    public static final String TEST_RECIPIENT_CACHE_KEY_PREFIX = "com:enableets:edu:package:ppr:test:recipient:";

    /**Cache Key Prefix：paper PaperCardBO*/
    public static final String PPR_PAPER_CARD_KEY_PREFIX = "com:enableets:edu:package:ppr:paper:paperCard:";

    /**Cache Key Prefix：ppr EnableCard*/
    public static final String PPR_ENABLE_CARD_KEY_PREFIX = "com:enableets:edu:package:ppr:enable:card";

    /** the action of question assignee */
    public static final String ACTIVITY_ASSIGNER_ACTION_QUESTION_MARK = "3";

    /** Activity Assessment Step: receive activity paper*/
    public static final String STEP_ACTION_RECEIVE_ACTIVITY_PAPER_TYPE = "2";

    /** Activity Assessment Step: answer paper type*/
    public static final String STEP_ACTION_ANSWER_PAPER_TYPE = "3";

    /** Activity Assessment Step: submit paper type*/
    public static final String STEP_ACTION_SUBMIT_PAPER_TYPE = "4";

    /** Activity Assessment Step: receive answer paper type*/
    public static final String STEP_ACTION_RECEIVE_ANSWER_TYPE = "5";

    /** Mark Over! Confirmation complete, modification complete identification, used for the previous review data has been submitted to determine the review status*/
    public static final Integer MARK_TYPE_ALL_COMPLETE = 3;

    /** Mark Over And Submit Answers*/
    public static final Integer MARK_TYPE_COMPLETE = 2;

    /** Assessment Activity Type*/
    public static final String ACTIVITY_TYPE_DEFAULT = "18";

    /** session key Teacher Grade Grade Grade Information */
    public static final String SESSION_KEY_TEACHER_BASE_INFO = "_TEACHER_SGS_BASE_INFO";
}
