package com.enableets.edu.pakage.manager.mark.service;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.module.cache.ICache;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.manager.mark.bo.TestInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.TestUserBO;
import com.enableets.edu.sdk.assessment.ITestInfoService;
import com.enableets.edu.sdk.assessment.ITestUserInfoService;
import com.enableets.edu.sdk.assessment.dto.QueryTAsTestResultDTO;
import com.enableets.edu.sdk.assessment.dto.TestUserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/04/03
 */
@Service
public class TestInfoService {

    @Autowired
    private ITestInfoService testInfoServiceSDK;

    @Autowired
    private ICache<String> testInfoCacheSupport;

    public TestInfoBO get(String testId, String activityId, String fileId) {
        return get(testId, activityId, fileId, false);
    }

    public TestInfoBO get(String testId, String activityId, String fileId, boolean includeTestUser){
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(testId)) sb.append(testId);
        else sb.append(activityId).append("&").append(fileId);
        if (includeTestUser) {
            sb.append(activityId).append("&").append(includeTestUser);
        }
        String testStr = testInfoCacheSupport.get(sb.toString());
        TestInfoBO test = null;
        if (StringUtils.isBlank(testStr)){
            QueryTAsTestResultDTO result = testInfoServiceSDK.getById(testId, activityId, fileId, includeTestUser);
            test = BeanUtils.convert(result, TestInfoBO.class);
            testInfoCacheSupport.put(test.getTestId(), JsonUtils.convert(test));
            if (includeTestUser) {
                testInfoCacheSupport.put(testStr, JsonUtils.convert(test));
            }
            testInfoCacheSupport.put(new StringBuffer(test.getActivityId()).append("&").append(test.getFileId()).toString(), JsonUtils.convert(test));
        } else {
            test = JsonUtils.convert(testStr, TestInfoBO.class);
        }
        return test;
    }


}
