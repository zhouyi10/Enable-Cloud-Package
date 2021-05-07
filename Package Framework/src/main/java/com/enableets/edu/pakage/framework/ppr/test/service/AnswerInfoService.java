package com.enableets.edu.pakage.framework.ppr.test.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerItem;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerQuestion;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestUserInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerCanvasInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.PackageUserAnswerInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerStampInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestUserInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerCanvasInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerStampInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.AnswerTrailBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.CanvasBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.PPRAnswerInfoBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.AutoMarkStrategyUtils;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestUserInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;
import com.enableets.edu.sdk.activity.dto.AddStepInstanceMarkInfoDTO;
import com.enableets.edu.sdk.activity.service.IStepTaskService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Answer related modules
 * @author walle_yu@enable-ets.com
 * @since 2020/07/22
 **/
@Service
public class AnswerInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnswerInfoService.class);

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private UserAnswerStampInfoDAO userAnswerStampInfoDAO;

    @Autowired
    private UserAnswerCanvasInfoDAO userAnswerCanvasInfoDAO;

    @Autowired
    private PackageUserAnswerInfoDAO packageUserAnswerInfoDAO;

    @Autowired
    private TestUserInfoDAO testUserInfoDAO;

    @Autowired
    private TestPaperService testPaperService;

    @Autowired
    private IStepTaskService stepTaskServiceSDK;

    @Autowired
    private ITokenGenerator tokenGenerator;

    //public String submit(CardBO cardBO, SubmitAttributeBO attribute) {
    public String submit(EnableCardPackage enableCardPackage, SubmitAttributeBO attribute){
        //1.get test info
        TestInfoBO testInfo = testInfoService.get(attribute.getTestId(), null, null, null, Boolean.FALSE);
        //2.get submit action info
        ActionItem action = enableCardPackage.getBody().getAction().getItems().stream().filter(e -> PPRConstants.STEP_ACTION_SUBMIT_PAPER_TYPE.equals(e.getType())).collect(Collectors.toList()).get(0);
        //3.get submit recipient info
        TestRecipientInfoBO recipient = RecipientCacheMap.get(testInfo.getTestId(), action.readProperty("userId"));
        //4.get paper question node
        Map<String, PaperNodeInfoBO> nodesMap = this.getQuestionNode(enableCardPackage.readRefId());
        List<PPRAnswerInfoBO> answers = this.convertAnswer(enableCardPackage.getBody().getAnswer());
        //5.do mark
        answers.forEach(answer -> {
            AutoMarkStrategyUtils.mark(answer, nodesMap.get(answer.getQuestionId()));
        });
        TestUserInfoBO testUserInfoBO = AutoMarkStrategyUtils.resetUserMarkStatus(answers);
        //6.TestUserPO
        TestUserInfoPO testUserInfoPO = buildTestUser(testInfo.getTestId(), recipient, testUserInfoBO, attribute, enableCardPackage.getBody().getAction().getItems());
        //7.TestUserAnswerPO
        List<UserAnswerInfoPO> userAnswerPOs = buildUserAnswers(answers, action.readProperty("userId"), enableCardPackage.readRefId(), testUserInfoPO.getTestUserId(), testInfo.getTestId(), nodesMap);
        //8.Batch Insert Stamps And Canvas, Insert userAnswer And TestUser Info
        insertAnswerCanvas(userAnswerPOs);
        insertAnswerStamps(userAnswerPOs);
        packageUserAnswerInfoDAO.insertList(userAnswerPOs);
        testUserInfoDAO.insertSelective(testUserInfoPO);
        //9.add Error Question Record

        //10. Callback Activity Refresh Total Score
        //SpringBeanUtils.getBean(SubmitAnswerService.class).replayStepSendMarkScore(testInfo.getTestId(), testInfo.getActivityId(), paperCardBO.getScore(), recipient.getUserId(), recipient.getUserName(), testUserInfoPO.getUserScore());
        this.replayStepSendMarkScore(attribute, testUserInfoPO.getUserScore());
        return testUserInfoPO.getTestUserId();
    }

    private List<PPRAnswerInfoBO> convertAnswer(Answer answer) {
        List<PPRAnswerInfoBO> answers = new ArrayList<>();
        for (AnswerItem answerItem : answer.getAnswerItems()) {
            for (AnswerQuestion aq : answerItem.getAnswers()) {
                PPRAnswerInfoBO as = new PPRAnswerInfoBO();
                as.setQuestionId(aq.getId());
                as.setParentId(Long.valueOf(answerItem.getId()));
                as.setAnswer(aq.getText());
                if (CollectionUtils.isNotEmpty(aq.getFiles())) {
                    List<CanvasBO> canvases = new ArrayList<>();
                    for (FileItem file : aq.getFiles()) {
                        CanvasBO cs = new CanvasBO();
                        cs.setFileId(file.getFileId());
                        cs.setFileName(file.getFileName());
                        cs.setUrl(file.getUrl());
                        canvases.add(cs);
                    }
                    as.setCanvases(canvases);
                }
                if (aq.getTrail() != null && CollectionUtils.isNotEmpty(aq.getTrail().getTimestamps())){
                    as.setTrails(BeanUtils.convert(aq.getTrail().getTimestamps(), AnswerTrailBO.class));
                }
                answers.add(as);
            }
        }
        return answers;
    }

    private void replayStepSendMarkScore(SubmitAttributeBO attribute, Float userScore){
        AddStepInstanceMarkInfoDTO markInfoDTO = new AddStepInstanceMarkInfoDTO();
        markInfoDTO.setScore(userScore);
        markInfoDTO.setStatus("1");  //pass
        stepTaskServiceSDK.editStateV2(attribute.getStepId(), attribute.getUserId(), markInfoDTO);
    }

    private void insertAnswerStamps(List<UserAnswerInfoPO> testUserAnswers) {
        List<UserAnswerStampInfoPO> answerStamps = new ArrayList<>();
        testUserAnswers.stream().filter(e -> CollectionUtils.isNotEmpty(e.getAnswerStamps())).map(e -> e.getAnswerStamps()).forEach(answerStamps :: addAll);
        if (CollectionUtils.isNotEmpty(answerStamps)){
            userAnswerStampInfoDAO.insertList(answerStamps);
        }
    }

    private void insertAnswerCanvas(List<UserAnswerInfoPO> testUserAnswers){
        List<UserAnswerCanvasInfoPO> canvases = new ArrayList<>();
        testUserAnswers.stream().filter(e -> CollectionUtils.isNotEmpty(e.getCanvases())).map(e -> e.getCanvases()).forEach(canvases :: addAll);
        if (CollectionUtils.isNotEmpty(canvases)){
            userAnswerCanvasInfoDAO.insertList(canvases);
        }
    }

    /**
     * Get Paper Question Node
     * @param paperId
     * @return
     */
    private Map<String, PaperNodeInfoBO> getQuestionNode(String paperId) {
        Map<String, PaperNodeInfoBO> nodesMap = new HashMap<>();
        try {
            PaperInfoBO paperInfoBO = testPaperService.get(paperId);
            for (PaperNodeInfoBO node : paperInfoBO.getNodes()) {
                if (node.getLevel()==1 || node.getLevel() == 2) continue;
                if (node.getLevel()==3){ //
                   nodesMap.put(node.getQuestion().getQuestionId(), node);
                }
                if (node.getLevel()==4){   //
                    nodesMap.put(node.getQuestion().getQuestionId(), node);
                }
            }
        }catch (Exception e){
            throw new MicroServiceException("38-01-007", "Get paper fail by ID '"+ paperId +"'");
        }
        return nodesMap;
    }

    /**
     *
     * @param testId
     * @param recipient
     * @param testUser
     * @return
     */
    private TestUserInfoPO buildTestUser(String testId, TestRecipientInfoBO recipient, TestUserInfoBO testUser, SubmitAttributeBO attributeBO, List<ActionItem> actions) {
        Date today = Calendar.getInstance().getTime();
        TestUserInfoPO testUserPO = new TestUserInfoPO();
        testUserPO.setTestUserId(tokenGenerator.getToken().toString());
        if ("_STEP".equals(attributeBO.getType()) && StringUtils.isNotBlank(attributeBO.getStepInstanceId())) {
            testUserPO.setActivityId(attributeBO.getStepInstanceId());
        } else if ("_ACTIVITY".equals(attributeBO.getType()) && StringUtils.isNotBlank(attributeBO.getActivityId())) {
            testUserPO.setActivityId(attributeBO.getActivityId());
        }
        testUserPO.setSchoolId(recipient.getSchoolId());
        testUserPO.setTermId(recipient.getTermId());
        testUserPO.setGradeId(recipient.getGradeCode());
        testUserPO.setClassId(recipient.getClassId());
        testUserPO.setGroupId(recipient.getGroupId());
        testUserPO.setGroupName(recipient.getGroupName());
        testUserPO.setUserId(recipient.getUserId());
        testUserPO.setUserName(recipient.getUserName());
        testUserPO.setTestId(testId);
        try{
            ActionItem action = getAction(PPRConstants.STEP_ACTION_ANSWER_PAPER_TYPE, actions);
            if (action != null) {
                testUserPO.setStartAnswerTime(new Date(action.readProperty("startTimestamp")));
                testUserPO.setEndAnswerTime(new Date(action.readProperty("endTimestamp")));
                testUserPO.setAnswerCostTime(Long.valueOf(action.readProperty("endTimestamp")) - Long.valueOf(action.readProperty("startTimestamp")));
            }
        }catch (Exception e){
            testUserPO.setAnswerCostTime(0L);
            testUserPO.setStartAnswerTime(today);
            testUserPO.setEndAnswerTime(today);
        }
        try{
            ActionItem action = getAction(PPRConstants.STEP_ACTION_SUBMIT_PAPER_TYPE, actions);
            if (action != null){
                testUserPO.setSubmitTime(new Date(action.readProperty("timestamp")));
            }
        }catch (Exception e){
            testUserPO.setSubmitTime(new Date());
        }
        testUserPO.setUserScore(testUser.getUserScore());
        testUserPO.setSubmitStatus("1");
        testUserPO.setMarkStatus(testUser.getMarkStatus());
        testUserPO.setCreator(testUserPO.getUserId());
        testUserPO.setUpdator(testUserPO.getUserId());
        testUserPO.setCreateTime(today);
        testUserPO.setUpdateTime(today);
        return testUserPO;
    }

    private ActionItem getAction(String name, List<ActionItem> actions){
        for (ActionItem action : actions) {
            if (action.getType().equals(name)){
                return action;
            }
        }
        return null;
    }

    private List<UserAnswerInfoPO> buildUserAnswers(List<PPRAnswerInfoBO> answers, String userId, String paperId, String testUserId, String testId, Map<String, PaperNodeInfoBO> questionNodes) {
        Date today = Calendar.getInstance().getTime();
        List<UserAnswerInfoPO> userAnswers = new ArrayList<>();
        for (PPRAnswerInfoBO answer : answers) {
            UserAnswerInfoPO userAnswer = new UserAnswerInfoPO();
            userAnswer.setAnswerId(tokenGenerator.getToken().toString());
            userAnswer.setParentId(answer.getParentId() == null ? null : answer.getParentId().toString());
            userAnswer.setTestUserId(testUserId);
            userAnswer.setUserId(userId);
            userAnswer.setTestId(testId);
            userAnswer.setExamId(paperId);
            userAnswer.setQuestionId(answer.getQuestionId());
            userAnswer.setUserAnswer(answer.getAnswer());
            userAnswer.setAnswerScore(answer.getAnswerScore());
            userAnswer.setAnswerStatus(answer.getAnswerStatus());
            try{
                long answerCostTime = 0L;
                List<AnswerTrailBO> trails = answer.getTrails();
                List<UserAnswerStampInfoPO> answerStamps = new ArrayList<>();
                for (AnswerTrailBO trail : trails) {
                    answerCostTime += trail.getEnd() - trail.getStart();
                    UserAnswerStampInfoPO stamp = new UserAnswerStampInfoPO();
                    stamp.setAnswerStampId(tokenGenerator.getToken().toString());
                    stamp.setAnswerId(userAnswer.getAnswerId());
                    stamp.setBeginTime(new Date(trail.getStart()));
                    stamp.setEndTime(new Date(trail.getEnd()));
                    stamp.setLastTime((trail.getEnd() - trail.getStart())/1000);
                    stamp.setQuestionId(answer.getQuestionId());
                    stamp.setTestId(testId);
                    stamp.setExamId(paperId);
                    stamp.setCreator(userId);
                    stamp.setCreateTime(today);
                    stamp.setUpdator(userId);
                    stamp.setUpdateTime(today);
                    answerStamps.add(stamp);
                }
                userAnswer.setAnswerStamps(answerStamps);
                userAnswer.setAnswerCostTime(answerCostTime/1000);
            }catch (Exception e){
                userAnswer.setAnswerCostTime(0L);
            }
            if (CollectionUtils.isNotEmpty(answer.getCanvases())){
                List<UserAnswerCanvasInfoPO> canvases = new ArrayList<UserAnswerCanvasInfoPO>();
                int order = 0;
                for (CanvasBO canvas : answer.getCanvases()) {
                    order++;
                    UserAnswerCanvasInfoPO canvasPO = new UserAnswerCanvasInfoPO();
                    canvasPO.setCanvasId(tokenGenerator.getToken().toString());
                    canvasPO.setAnswerId(userAnswer.getAnswerId());
                    canvasPO.setCanvasType("0");
                    canvasPO.setCanvasOrder(order);
                    canvasPO.setCreator(userId);
                    canvasPO.setCreateTime(today);
                    canvasPO.setUpdator(userId);
                    canvasPO.setUpdateTime(today);
                    canvasPO.setFileId(canvas.getFileId());
                    canvasPO.setFileName(canvas.getFileName());
                    canvasPO.setUrl(canvas.getUrl());
                    canvases.add(canvasPO);
                    canvasPO = new UserAnswerCanvasInfoPO();
                    canvasPO.setCanvasId(tokenGenerator.getToken().toString());
                    canvasPO.setAnswerId(userAnswer.getAnswerId());
                    canvasPO.setCanvasType("1");
                    canvasPO.setCanvasOrder(order);
                    canvasPO.setCreator(userId);
                    canvasPO.setCreateTime(today);
                    canvasPO.setUpdator(userId);
                    canvasPO.setUpdateTime(today);
                    canvasPO.setFileId(canvas.getFileId());
                    canvasPO.setFileName(canvas.getFileName());
                    canvasPO.setUrl(canvas.getUrl());
                    canvases.add(canvasPO);
                }
                userAnswer.setCanvases(canvases);
            }
            userAnswer.setMarkStatus(answer.getMarkStatus());
            userAnswer.setQuestionScore(questionNodes.get(answer.getQuestionId()).getPoints());
            userAnswer.setCreator(userId);
            userAnswer.setCreateTime(today);
            userAnswer.setUpdator(userId);
            userAnswer.setUpdateTime(today);
            userAnswers.add(userAnswer);
        }
        return userAnswers;
    }
}
