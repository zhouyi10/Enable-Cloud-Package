package com.enableets.edu.pakage.manager.core;

/**
 * Static Variable
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
public class Constants {

    public static final String CONTEXT_PATH = "/manager/package";

    /** Cache Expire Time : 30 Min*/
    public static final Long DEFAULT_REDIS_CACHE_EXPIRE_TIME = 30 * 60L;

    /** Default date and time format */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** Default time zone */
    public static final String DEFAULT_DATE_ZONE = "GMT+8";

    public static final String CONTENT_PUBLIC_TYPE = "R01";

    public static final String CONTENT_SCHOOL_TYPE = "R02";

    public static final String CONTENT_PRIVATE_TYPE = "R00";

    public static final String DEFAULT_SCHOOL = "999999";

    /**content:stage type*/
    public static final String CONTENT_DICTIONARY_TYPE_STAGE = "02";

    /**content:grade type*/
    public static final String CONTENT_DICTIONARY_TYPE_GRADE = "03";

    /**content:subject type*/
    public static final String CONTENT_DICTIONARY_TYPE_SUBJECT = "01";

    /**content:materialVersion type*/
    public static final String CONTENT_DICTIONARY_TYPE_MATERIAL_VERSION = "12";

    /**content:ability type*/
    public static final String CONTENT_DICTIONARY_TYPE_ABILITY = "09";

    /**content:questionType type*/
    public static final String CONTENT_DICTIONARY_TYPE_QUESTION_TYPE = "11";

    /**content:difficulty type*/
    public static final String CONTENT_DICTIONARY_TYPE_DIFFICULTY = "08";

    /** Content: type Code*/
    public static final String CONTENT_TYPE_EXAM = "C15";

    /***/
    public static final String HEAD_X_FRAME_OPTIONS = "X-Frame-Options";

    /***/
    public static final String HEAD_ALLOWALL = "ALLOWALL";

    public static final String MODEL_KEY_CONTENT_MANAGER_URL = "contentManagerUrl";

    /** Course type:obligatory */
    public static final String COURSE_SELECTION_TYPE_REQUIRED = "1";

    /**teaching methods: Offline */
    public static final String COURSE_TEACHING_METHOD_OFFLINE = "2";

}
