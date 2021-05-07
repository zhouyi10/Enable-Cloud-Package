package com.enableets.edu.sdk.ppr.ppr.core;

import java.io.File;

/**
 * static constant
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
public class Constants {

    public static final String ENCODING = "encoding";
    public static final String VERIFICATION = "[verification method]";
    public static final String ENCRYPTION = "[encryption]";
    public static final String COMPRESSION = "[compression method]";
    public static final int TEST_PART_LEVEL = 1;
    public static final int SECTION_LEVEL = 2;
    public static final int QUESTION_LEVEL = 3;
    public static final int QUESTION_CHILD_LEVEL = 4;
    public static final String URL_PPR_ZIP_PATH_PREFIX = "/resource/url/";
    public static final String FILE_ID_PPR_ZIP_PATH_PREFIX = "/resource/fileId/";
    public static final String PPR_ZIP_SUFFIX = ".ppr";
    public static final String PPR_PAPER_XML_PATH = File.separator + "ppr.xml";
    public static final String PPR_PAPER_CARD_XML_PATH = File.separator + "card.xml";
    /* receive test papers */
    public static final String ACTION_RECEIVE_TYPE_CODE = "receive";
    public static final String ACTION_ANSWER_TYPE_CODE = "answer";
    public static final String ACTION_SUBMIT_TYPE_CODE = "submit";
    public static final String ACTION_MARK_TYPE_CODE = "mark";
    public static final String ACTION_CHECK_SCORE_TYPE_CODE = "checkScore";

    public static final String XML_CANNOT_ROOT_NODE = "Xml root node cannot found!";

}
