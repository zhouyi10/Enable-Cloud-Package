package com.enableets.edu.pakage.manager.mark.core;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.manager.mark.bo.MarkingCacheBO;
import com.enableets.edu.pakage.manager.mark.bo.TestInfoBO;
import com.enableets.edu.pakage.manager.mark.bo.TestRecipientBO;
import com.enableets.edu.pakage.manager.mark.service.TestInfoService;
import com.enableets.edu.sdk.assessment.IQuestionAssignmentService;
import com.enableets.edu.sdk.assessment.dto.QueryQuestionAssignmentRecipientResultDTO;
import com.enableets.edu.sdk.assessment.dto.QueryQuestionAssignmentResultDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
public class AssignerQuestionCheckMarking extends AbstractCheckMarking {

    private List<QueryQuestionAssignmentResultDTO> questionAssignments;

    public AssignerQuestionCheckMarking(String testId, RedisSupportExtendCache<String> markTestCacheSupport, String loginUserId) {
        super(testId, markTestCacheSupport, loginUserId);
        init();
    }

    /**
     *  题目有交集 && 批阅学生有交集  表示 有重复批阅 禁止打卡批阅页面
     * @return
     */
    @Override
    public boolean check() {
        //验证题目
        boolean questionRepeat = false;
        for (MarkingCacheBO markingCache : markingCaches) {
            if (markingCache.getQuestionIds().equals("_all")) {
                questionRepeat = true; break;
            }
            for (QueryQuestionAssignmentResultDTO questionAssignment : questionAssignments) {
                if (markingCache.getQuestionIds().indexOf(questionAssignment.getQuestionId()) > -1){
                    questionRepeat = true; break;
                }
            }
            if (questionRepeat) break;
        }
        boolean studentRepeat = isStudentRepeat();
        if (questionRepeat && studentRepeat) return true;
        addCache();
        return false;
    }

    @Override
    public List<String> getMarkArea() {
        List<String> areas = new ArrayList<>();
        Map<String, String> groupMap = initGroup();
        for (QueryQuestionAssignmentRecipientResultDTO recipient : questionAssignments.get(0).getRecipients()) {
            areas.add(new StringBuilder("_all").append("_").append(groupMap.get(recipient.getUserId())).append("_").append(recipient.getUserId()).toString());
        }
        return areas;
    }

    @Override
    public String getMarkQuestion() {
        return questionAssignments.stream().map(e -> e.getQuestionId()).reduce((x, y) -> x + "," + y).get();
    }

    private void init(){
        IQuestionAssignmentService questionAssignmentServiceSDK = SpringBeanUtils.getBean(IQuestionAssignmentService.class);
        questionAssignments = questionAssignmentServiceSDK.get(testId, loginUserId);
    }

    private Map<String, String> initGroup(){
        TestInfoService testInfoService = SpringBeanUtils.getBean(TestInfoService.class);
        TestInfoBO testInfoBO = testInfoService.get(testId, null, null, true);
        List<TestRecipientBO> recipients = testInfoBO.getRecipients();
        Map<String, String> map = new HashMap<>();
        for (TestRecipientBO recipient : recipients) {
            map.put(recipient.getUserId(), recipient.getGroupId());
        }
        return map;
    }
}
