package com.enableets.edu.pakage.framework.ppr.test.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestUserInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerCanvasInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.PackageUserAnswerInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerStampInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.TAsUserAnswerStampPO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestUserInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerCanvasInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerInfoPO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QueryMarkAnswerBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestUserInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerCanvasInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerStampBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConfigReader;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.paper.service.PaperInfoService;
import com.enableets.edu.sdk.activity.dto.AddStepInstanceMarkInfoDTO;
import com.enableets.edu.sdk.activity.service.IStepTaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import tk.mybatis.mapper.entity.Example;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/18
 **/
@Service
public class TestUserInfoService {

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private PaperInfoService paperInfoService;

    @Autowired
    private PPRConfigReader pprConfigReader;

    @Autowired
    private IStepTaskService stepTaskServiceSDK;

    @Autowired
    private TestUserInfoDAO testUserInfoDAO;

    @Autowired
    private PackageUserAnswerInfoDAO packageUserAnswerInfoDAO;

    @Autowired
    private UserAnswerCanvasInfoDAO userAnswerCanvasInfoDAO;

    @Autowired
    private UserAnswerStampInfoDAO userAnswerStampInfoDAO;

    /**
     * Query Answer By Marking Condition
     * @param queryMarkAnswerBO
     * @return
     */
    public List<TestUserInfoBO> queryAnswer(QueryMarkAnswerBO queryMarkAnswerBO){
        if (StringUtils.isBlank(queryMarkAnswerBO.getTestId())){
            if (StringUtils.isBlank(queryMarkAnswerBO.getExamId())){
                Assert.notNull(queryMarkAnswerBO.getActivityId(), "activityId is null!");
                Assert.notNull(queryMarkAnswerBO.getFileId(), "fileId is null!");
            }
        }
        //1.查询所有交卷信息包含答案信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("testId", queryMarkAnswerBO.getTestId());
        params.put("activityId", queryMarkAnswerBO.getActivityId());
        params.put("fileId", queryMarkAnswerBO.getFileId());
        params.put("examId", queryMarkAnswerBO.getExamId());
        params.put("userId", queryMarkAnswerBO.getUserId());
        if (StringUtils.isNotBlank(queryMarkAnswerBO.getGroupIds())){
            List<String> groups = Arrays.asList(queryMarkAnswerBO.getGroupIds().split(","));
            params.put("groups", groups);
        }
        if (StringUtils.isNotBlank(queryMarkAnswerBO.getQuestionIds())){
            params.put("questions", Arrays.asList(queryMarkAnswerBO.getQuestionIds().split(",")));
        }
        List<TestUserInfoPO> testUsers = testUserInfoDAO.queryAnswer(params);
        List<TestUserInfoBO> retTestUsers = new ArrayList<>();
        List<String> testIds = testUsers.stream().map(testUser -> testUser.getTestId()).distinct().collect(Collectors.toList());
        for (String tmpTestId : testIds) {
            List<TestUserInfoPO> tmpTestUsers = testUsers.stream().filter(testUser -> testUser.getTestId().equals(tmpTestId)).collect(Collectors.toList());
            List<String> userList = tmpTestUsers.stream().map(testUser -> testUser.getUserId()).distinct().collect(Collectors.toList());
            for (String userId : userList) {
                List<TestUserInfoPO> userTestUsers = tmpTestUsers.stream().filter(testUser -> testUser.getUserId().equals(userId)).collect(Collectors.toList());
                if (userTestUsers.size() == 1) retTestUsers.add(BeanUtils.convert(userTestUsers.get(0), TestUserInfoBO.class));
                else{
                    userTestUsers.sort((o1, o2) -> {
                        long time1 = o1.getCreateTime().getTime();
                        long time2 = o2.getCreateTime().getTime();
                        if (time1 < time2) return -1;
                        else if(time1 == time2) return 0;
                        else return 1;
                    });
                    retTestUsers.add(BeanUtils.convert(userTestUsers.get(userTestUsers.size()-1), TestUserInfoBO.class));
                }
            }
        }
        return BeanUtils.convert(retTestUsers, TestUserInfoBO.class);
    }

    public void mark(String testId, Integer type, List<UserAnswerInfoBO> answers) {
        Assert.notNull("type", "type is null!");
        if (type != PPRConstants.MARK_TYPE_ALL_COMPLETE) Assert.notEmpty(answers, "No Answers Info!");
        List<String> testUserIds = new ArrayList<String>();
        List<String> userIds = new ArrayList<>();
        if (type == PPRConstants.MARK_TYPE_ALL_COMPLETE) {
            List<TestUserInfoPO> testUsers = testUserInfoDAO.getByTestId(testId);
            if (!CollectionUtils.isEmpty(testUsers)) {
                for (TestUserInfoPO testUser : testUsers) {
                    testUserIds.add(testUser.getTestUserId());
                    userIds.add(testUser.getUserId());
                }
            }
        } else {
            for (UserAnswerInfoBO answer : answers) {
                if (testUserIds.contains(answer.getTestUserId())) continue;
                testUserIds.add(answer.getTestUserId());
            }
            for (UserAnswerInfoBO answer : answers) {
                UserAnswerInfoPO answerPO = new UserAnswerInfoPO();
                answerPO.setAnswerScore(answer.getAnswerScore());
                answerPO.setAnswerStatus(answer.getAnswerStatus());
                answerPO.setMarkStatus(answer.getMarkStatus());
                answerPO.setUpdateTime(new Date());
                Example example = new Example(UserAnswerInfoPO.class);
                example.createCriteria().andEqualTo("answerId", answer.getAnswerId());
                packageUserAnswerInfoDAO.updateByExampleSelective(answerPO, example);
            }
            // 加入错题集
            userIds = answers.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        }
        if (type != null && (type == PPRConstants.MARK_TYPE_COMPLETE || type == PPRConstants.MARK_TYPE_ALL_COMPLETE)) {
            testUserInfoDAO.completeMark(testUserIds);
        } else {
            testUserInfoDAO.recalculateTotalScore(testUserIds);
        }
        //缓存已批阅信息
        //this.cacheAddMarkStatus(testId, userIds);
        if (type != null && (type == PPRConstants.MARK_TYPE_COMPLETE || type == PPRConstants.MARK_TYPE_ALL_COMPLETE)) { //批阅完成通知活动批阅
            sendMarkInfo(testId, testUserIds);
        }
    }

    public void editCanvasInfo(UserAnswerCanvasInfoBO userAnswerCanvasInfoBO){
        UserAnswerCanvasInfoPO canvas = BeanUtils.convert(userAnswerCanvasInfoBO, UserAnswerCanvasInfoPO.class);
        canvas.setUpdator(userAnswerCanvasInfoBO.getUserId());
        canvas.setUpdateTime(new Date());
        Example example = new Example(UserAnswerCanvasInfoPO.class);
        example.createCriteria().andEqualTo("canvasId", userAnswerCanvasInfoBO.getCanvasId());
        userAnswerCanvasInfoDAO.updateByExampleSelective(canvas, example);
    }

    private void sendMarkInfo(String testId, List<String> testUserIds) {
        TestInfoBO testInfo = testInfoService.get(testId, null, null, null, false);
        List<String> activityTypesOfStep = Arrays.asList(pprConfigReader.getActivityTypeStepArr());
        if (!PPRConstants.ACTIVITY_TYPE_DEFAULT.equals(testInfo.getActivityType()) && !activityTypesOfStep.contains(testInfo.getActivityType())) {
            return;
        }
        List<TestUserInfoPO> users = testUserInfoDAO.getByTestId(testId);
        for(TestUserInfoPO user : users){
            for(String testUserId : testUserIds){
                if(!testUserId.equals(user.getTestUserId())) continue;
                // 1 不回复学习活动的测验 活动标识为 testUserId_activityId  2 回复学习活动但出错的 为 testUserId 3 回复活动成功的为回复活动标识
                if (user.getActivityId().indexOf(user.getTestUserId()) >= 0) {
                    continue;
                }
                AddStepInstanceMarkInfoDTO markInfoDTO = new AddStepInstanceMarkInfoDTO();
                markInfoDTO.setScore(user.getScore());
                markInfoDTO.setStatus("1");  //pass
                stepTaskServiceSDK.editStateV2(testInfo.getActivityId(), user.getUserId(), markInfoDTO);
            }
        }
    }

    public List<UserAnswerStampBO> getAnswerTracks(String testUserId) {
        List<TAsUserAnswerStampPO> userAnswerStamps = userAnswerStampInfoDAO.getUserAnswerStamps(testUserId);
        if (CollectionUtils.isEmpty(userAnswerStamps)) return Collections.EMPTY_LIST;
        List<UserAnswerStampBO> result = BeanUtils.convert(userAnswerStamps, UserAnswerStampBO.class);
        String examId = result.get(0).getExamId();
        PaperInfoBO paperInfoBO = paperInfoService.get(examId);
        if (paperInfoBO != null && !CollectionUtils.isEmpty(paperInfoBO.getNodes())){
            Set<Integer> filterLevel = new HashSet<Integer>(Arrays.asList(1, 2, 3, 4));
            Map<String, String> questionNoMap = new HashMap<>();
            Integer oneLevelName = 0;
            Integer twoLevelName = 0;
            Integer threeLevelName = 0;
            Integer fourLevelName = 0;
            for (PaperNodeInfoBO node : paperInfoBO.getNodes()) {
                if (node.getLevel() == 1) {
                    oneLevelName += 1;
                    twoLevelName = 0;
                } else if (node.getLevel() == 2) {
                    twoLevelName += 1;
                    threeLevelName = 0;
                } else if (node.getLevel() == 3) {
                    threeLevelName += 1;
                    fourLevelName = 0;
                    String value = oneLevelName + "," + twoLevelName + "," + threeLevelName;
                    questionNoMap.put(node.getQuestion().getQuestionId(), value);
                } else if (node.getLevel() == 4) {
                    fourLevelName += 1;
                    String value = oneLevelName + "," + twoLevelName + "," + threeLevelName + "," + fourLevelName;
                    questionNoMap.put(node.getQuestion().getQuestionId(), value);
                }
            }

            for (UserAnswerStampBO bo : result) {
                if (questionNoMap.containsKey(bo.getQuestionId())) {
                    bo.setQuestionNo(questionNoMap.get(bo.getQuestionId()));
                }
            }
        }
        return result;
    }
}
