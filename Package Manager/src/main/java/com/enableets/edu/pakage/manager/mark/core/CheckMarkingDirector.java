package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/24
 **/
public class CheckMarkingDirector {

    private final static String ASSIGN_GROUP_ACTION_TYPE = "1";

    private final static String ASSIGN_QUESTION_ACTION_TYPE = "3";

    private final String testId;

    private final String activityId;

    private String groupId;

    private String userId;

    private String actionType;

    private final String loginUserId;

    RedisSupportExtendCache<String> markTestCacheSupport;

    public CheckMarkingDirector(String testId, String activityId, String groupId, String userId, String actionType, String loginUserId){
        this.testId = testId;
        this.activityId = activityId;
        this.groupId = groupId;
        this.userId = userId;
        this.actionType = actionType;
        this.loginUserId = loginUserId;
        markTestCacheSupport = SpringBeanUtils.getBean("markTestCacheSupport");
    }

    public boolean check(){
        if (markTestCacheSupport.contains(getKey())) return true;
        boolean doMarking = false;
        if (StringUtils.isNotBlank(actionType) && actionType.equals(ASSIGN_QUESTION_ACTION_TYPE)){
            doMarking = new AssignerQuestionCheckMarking(testId, markTestCacheSupport, loginUserId).check();
        }else if (StringUtils.isNotBlank(actionType) && actionType.equals(ASSIGN_GROUP_ACTION_TYPE)){
            doMarking = new AssignerGroupCheckMarking(testId, markTestCacheSupport, loginUserId, activityId).check();
        }else{
            doMarking = new DefaultCheckMarking(testId, markTestCacheSupport, loginUserId, groupId, userId).check();
        }
        return doMarking;
    }

    private String getKey(){
        return new StringBuilder(testId).append("_").append(loginUserId).toString();
    }
}
