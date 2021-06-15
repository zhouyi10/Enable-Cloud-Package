package com.enableets.edu.pakage.framework.ppr.test.service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.framework.core.RabbitMQConfig;
import com.enableets.edu.pakage.framework.ppr.bo.*;
import com.enableets.edu.pakage.framework.ppr.core.PPRConfigReader;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.paper.service.PaperInfoService;
import com.enableets.edu.pakage.framework.ppr.test.bo.TestMarkResultInfoBO;
import com.enableets.edu.pakage.framework.ppr.test.dao.PackageUserAnswerInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestUserInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerCanvasInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerStampInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.TAsUserAnswerStampPO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestUserInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerCanvasInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerInfoPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

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
    private TestUserInfoDAO testUserInfoDAO;

    @Autowired
    private PackageUserAnswerInfoDAO packageUserAnswerInfoDAO;

    @Autowired
    private UserAnswerCanvasInfoDAO userAnswerCanvasInfoDAO;

    @Autowired
    private UserAnswerStampInfoDAO userAnswerStampInfoDAO;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Query Answer By Marking Condition
     * @param queryMarkAnswerBO
     * @return
     */
    public List<TestUserInfoBO> queryAnswer(QueryMarkAnswerBO queryMarkAnswerBO){
        if (StringUtils.isBlank(queryMarkAnswerBO.getTestId())){
            if (StringUtils.isBlank(queryMarkAnswerBO.getExamId())){
                Assert.notNull(queryMarkAnswerBO.getStepId(), "stepId is null!");
                Assert.notNull(queryMarkAnswerBO.getFileId(), "fileId is null!");
            }
        }
        //1.查询所有交卷信息包含答案信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("testId", queryMarkAnswerBO.getTestId());
        params.put("stepId", queryMarkAnswerBO.getStepId());
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

    public TestMarkResultInfoBO mark(String testId, Integer type, List<UserAnswerInfoBO> answers) {
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
        if (type != null && (type == PPRConstants.MARK_TYPE_COMPLETE || type == PPRConstants.MARK_TYPE_ALL_COMPLETE)) { //批阅完成通知活动批阅
            return sendMarkInfo(testId, testUserIds);
        }
        return null;
    }

    public void editCanvasInfo(UserAnswerCanvasInfoBO userAnswerCanvasInfoBO){
        UserAnswerCanvasInfoPO canvas = BeanUtils.convert(userAnswerCanvasInfoBO, UserAnswerCanvasInfoPO.class);
        canvas.setUpdator(userAnswerCanvasInfoBO.getUserId());
        canvas.setUpdateTime(new Date());
        Example example = new Example(UserAnswerCanvasInfoPO.class);
        example.createCriteria().andEqualTo("canvasId", userAnswerCanvasInfoBO.getCanvasId());
        userAnswerCanvasInfoDAO.updateByExampleSelective(canvas, example);
    }

    private TestMarkResultInfoBO sendMarkInfo(String testId, List<String> testUserIds) {
        TestInfoBO testInfoBO = testInfoService.get(testId);
        List<TestUserInfoPO> testUsers = testUserInfoDAO.selectByIds(testUserIds);
        TestMarkResultInfoBO resultInfoBO = new TestMarkResultInfoBO();
        resultInfoBO.setStepId(testInfoBO.getStepId()).setActivityId(testInfoBO.getActivityId()).setFileId(testInfoBO.getFileId());
        List<TestMarkResultInfoBO.UserScoreBO> list = testUsers.stream().map(e -> new TestMarkResultInfoBO.UserScoreBO(e.getTestUserId(), e.getUserId(), e.getUserScore())).collect(Collectors.toList());
        resultInfoBO.setUsers(list);
        return resultInfoBO;
    }

    public void notifyStepTaskMarkResult(String testId, String stepId, String activityType, String userId, String testUserId, Float score){
//        Map<String, Object> messages = new HashMap<>();
//        messages.put("testId", testId);
//        messages.put("stepId", stepId);
//        messages.put("activityType", activityType);
//        messages.put("userId", userId);
//        messages.put("testUserId", testUserId);
//        messages.put("score", score);
        //rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_USER_ASSESSMENT_RESULT, RabbitMQConfig.ROUTING_KEY_USER_ASSESSMENT_RESULT, messages);
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
