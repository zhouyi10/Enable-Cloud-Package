package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.manager.mark.bo.MarkingCacheBO;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public abstract class AbstractCheckMarking implements ICheckMarking {

    public final String testId;

    public RedisSupportExtendCache<String> markTestCacheSupport;

    public String loginUserId;

    public List<MarkingCacheBO> markingCaches;

    public AbstractCheckMarking(String testId, RedisSupportExtendCache<String> markTestCacheSupport, String loginUserId){
        this.testId = testId;
        this.markTestCacheSupport = markTestCacheSupport;
        this.loginUserId = loginUserId;
        getTestAllCache();
    }

    public abstract List<String> getMarkArea();

    public abstract String getMarkQuestion();

    public boolean isStudentRepeat(){
        boolean isRepeat = false;
        for (MarkingCacheBO cacheBO : markingCaches) {
            for (String beMarkingUser : getMarkArea()) {
                if (beMarkingUser.equals("_all")) {
                    isRepeat = true; break;
                }
                for (String markingUser : cacheBO.getUserSearchCode()) {
                    if (markingUser.equals("_all") || beMarkingUser.indexOf(markingUser) > -1 || markingUser.indexOf(beMarkingUser) > -1){
                        isRepeat = true; break;
                    }
                }
                if (isRepeat) break;
            }
            if (isRepeat) break;
        }
        return isRepeat;
    }

    public String getKey(){
        return new StringBuilder(testId).append("_").append(loginUserId).toString();
    }

    public void addCache(){
        markTestCacheSupport.put(getKey(), JsonUtils.convert(getNewMarkingCache()));
    }

    private void getTestAllCache(){
        markingCaches = new ArrayList<>();
        Set<String> keys = markTestCacheSupport.keys(this.testId);
        if (CollectionUtils.isNotEmpty(keys)){
            markingCaches = new ArrayList<>();
            for (String key : keys) {
                markingCaches.add(JsonUtils.convert(markTestCacheSupport.getByCompleteKey(key), MarkingCacheBO.class));
            }
        }
    }

    private MarkingCacheBO getNewMarkingCache(){
        MarkingCacheBO cacheBO = new MarkingCacheBO();
        cacheBO.setQuestionIds(getMarkQuestion());
        cacheBO.setUserSearchCode(getMarkArea());
        return cacheBO;
    }
}
