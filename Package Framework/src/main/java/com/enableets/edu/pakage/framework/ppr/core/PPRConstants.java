package com.enableets.edu.pakage.framework.ppr.core;

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

    /** Package - ppr cache key prefix*/
    public static final String PACKAGE_PPR_CACHE_KEY_PREFIX = "com:enableets:edu:package:ppr:";

    /** the action of question assignee */
    public static final String ACTIVITY_ASSIGNER_ACTION_QUESTION_MARK = "3";

    /** Mark Over! Confirmation complete, modification complete identification, used for the previous review data has been submitted to determine the review status*/
    public static final Integer MARK_TYPE_ALL_COMPLETE = 3;

    /** Mark Over And Submit Answers*/
    public static final Integer MARK_TYPE_COMPLETE = 1;

    /** PPR Use Code*/
    public static final String DEFAULT_PPR_USE_CODE = "assessment";

    /** PPR Question Kind*/
    public static final String PPR_QUESTION_KIND = "4";

    /** Step Action type*/
    public static final String ANSWER_CARD_ACTION_SUBMIT = "submit";

    /**(word, pdf, mp3) document file paper*/
    public static final String ATTACHMENT_FILE_PAPER = "1";

    /** Default Paper (Question paper)*/
    public static final String DEFAULT_QUESTION_PAPER = "2";

    /** micro course Time-sharing answer Paper*/
    public static final String TIME_SHARING_PAPER = "3";

    /** Frame choose paper*/
    public static final String IMG_FRAME_CHOOSE_PAPER = "4";

    public static final String XKW_INTERFACE_ERROR_CODE = "40001";

    /** JSON字符串头尾 */
    public static final String JSON_HAND_TAIL = "\\\"";

    /** HTTP 头 */
    public static final String HTTP_HAND = "http";
}
