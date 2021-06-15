package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.sdk.activity.dto.ActivityAssigner2GroupDTO;
import com.enableets.edu.sdk.activity.service.IActivityInfoService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public class AssignerGroupCheckMarking extends AbstractCheckMarking {

    private String activityId;

    private List<ActivityAssigner2GroupDTO> assigners;

    public AssignerGroupCheckMarking(String testId, RedisSupportExtendCache<String> markTestCacheSupport, String loginUserId, String activityId) {
        super(testId, markTestCacheSupport, loginUserId);
        this.activityId = activityId;
        this.loginUserId = loginUserId;
        init();
    }

    /***
     * 题目有交集 && 批阅学生有交集  表示 有重复批阅 禁止打卡批阅页面
     * 指派班级批阅下是所有题目 所以:题目是一定有交集的，不验证； 验证是否有重复的学生即可
     * @return
     */
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
        for (ActivityAssigner2GroupDTO assigner : assigners) {
            areas.add(new StringBuilder("_all").append("_").append(assigner.getGroupId()).toString());
        }
        return areas;
    }

    @Override
    public String getMarkQuestion() {
        return "_all";
    }

    private void init(){
        IActivityInfoService activityInfoServiceSDK = SpringBeanUtils.getBean(IActivityInfoService.class);
        assigners = activityInfoServiceSDK.getActivityAssignerInfoV2(activityId, null, loginUserId, "1", null, null);
    }
}
