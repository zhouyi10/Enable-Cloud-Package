package com.enableets.edu.pakage.framework.ppr.test.core;

import com.enableets.edu.pakage.framework.core.Constants;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/05/12
 **/

public class TestConstants {

    /** 时间格式*/
    public static final String DATE_FORMAT_TYPE = "yyyy-MM-dd HH:mm:ss";

    /** 错误码格式模版-系统级(1)、服务级(2)+模块编码(2位数字)+模块错误编码(2位数字) */
    public static final String ERROR_CODE_FORMAT = "%s%s%s";

    /** 系统级错误编码 */
    public static final String SYSTEM_ERROR = "1";

    /** 服务级错误编码 */
    public static final String SERVICE_ERROR = "2";

    /** 测验 错误码 */
    public static final String ERROR_CODE_SAVE_USER_ANSWER_FAILED = String.format(ERROR_CODE_FORMAT, SERVICE_ERROR, Constants.MODEL_TEST, "06");

    public static final String ERROR_CODE_GET_SUBMIT_ANSWER_DATA_FAILED = String.format(ERROR_CODE_FORMAT, SERVICE_ERROR, Constants.MODEL_TEST, "01");

    /** 测验未发布给该学生 错误信息 */
    public static final String ERROR_MESSAGE_SAVE_USER_ANSWER_FAILED = "测验信息保存失败!";

    public static final String ERROR_MESSAGE_GET_SUBMIT_ANSWER_DATA_FAILED = "获取交卷数据失败!";

    public static final String SUBMIT_ERROR_LOG = "submit-error-log";

    /** 线程池出错错误码 */
    public static final String ERROR_CODE_THREAD_POOL_ERROR = String.format(ERROR_CODE_FORMAT, SYSTEM_ERROR, "00", "10");

    /** 线程池出错错误码 */
    public static final String ERROR_MESSAGE_THREAD_POOL_ERROR = "线程池处理异常";

    public static final String CACHE_PREFIX = "com:enableets:edu:package:ppr:assessment:";

    public static final int CACHE_ONE_WEEK = 7 * 24 * 60 * 60;
}
