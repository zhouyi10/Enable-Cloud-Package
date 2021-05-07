package com.enableets.edu.pakage.framework.ppr.test.service.submit.utils;

/**
 * @author duffy_ding
 * @since 2020/03/16
 */
public class SubmitUtils {

    /**
     * construct business identity
     * @param testId
     * @param userId
     * @return business id
     */
    public static String buildAnswerBusinessId(String testId, String userId) {
        return testId + "_" + userId;
    }
}
