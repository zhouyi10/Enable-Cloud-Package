package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.manager.mark.bo.TestInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.TestRecipientBO;
import com.enableets.edu.pakage.manager.mark.service.TestInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public class DefaultCheckMarking extends AbstractCheckMarking {

    private String groupId;

    private String userId;

    public DefaultCheckMarking(String testId, RedisSupportExtendCache<String> markTestCacheSupport, String loginUserId, String groupId, String userId) {
        super(testId, markTestCacheSupport, loginUserId);
        this.groupId = groupId;
        this.userId = userId;
    }

    /**
     *  题目有交集 && 批阅学生有交集  表示 有重复批阅 禁止打卡批阅页面
     *  默认批阅下是所有题目 所以:题目是一定有交集的，不验证； 验证是否有重复的学生即可
     * */
    @Override
    public boolean check() {
        //验证学生
        boolean repeat = isStudentRepeat();
        if (!repeat) addCache();
        return repeat;
    }

    @Override
    public List<String> getMarkArea() {
        List<String> areas = new ArrayList<>();
        StringBuilder sb = new StringBuilder("_all");
        if (StringUtils.isNotBlank(groupId)) sb.append("_").append(groupId);
        if (StringUtils.isNotBlank(userId)){
            if (StringUtils.isBlank(groupId)){
                TestInfoService testInfo = SpringBeanUtils.getBean(TestInfoService.class);
                TestInfoBO testInfoBO = testInfo.get(testId, null, null, true);
                if (testInfoBO != null && CollectionUtils.isNotEmpty(testInfoBO.getRecipients())) {
                    List<TestRecipientBO> recipients = testInfoBO.getRecipients();
                    for (TestRecipientBO recipient : recipients) {
                        if (recipient.getUserId().equals(userId)){
                            groupId = recipient.getGroupId(); break;
                        }
                    }
                }
                if (StringUtils.isNotBlank(groupId)) sb.append("_").append(groupId);
                sb.append("_").append(userId);
            }
        }
        areas.add(sb.toString());
        return areas;
    }

    @Override
    public String getMarkQuestion() {
        return "_all";
    }
}
