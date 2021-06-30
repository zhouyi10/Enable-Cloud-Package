package com.enableets.edu.pakage.framework.ppr.test.service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerItem;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerQuestion;
import com.enableets.edu.pakage.framework.ppr.bo.*;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;
import com.enableets.edu.pakage.framework.ppr.test.dao.PackageUserAnswerInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestUserInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.UserAnswerCanvasInfoDAO;
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
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import com.enableets.edu.sdk.steptask.dto.AddStepRecordDTO;
import com.enableets.edu.sdk.steptask.dto.AddStepRecordListDTO;
import com.enableets.edu.sdk.steptask.service.IStepRecordV2Service;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private TestUserInfoService testUserInfoService;

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private IStepRecordV2Service iStepRecordV2ServiceSDK;

    @Autowired
    private ITokenGenerator tokenGenerator;

    public String submit(String answerCardXml) {
        PPRPackageLifecycle lifecycle = new PPRPackageLifecycle();
        EnableCardPackage cardPackage = lifecycle.parse(answerCardXml);
        SubmitAttributeBO attributeBO = new SubmitAttributeBO();
        attributeBO.setTestUserId(tokenGenerator.getToken().toString());
        return this.submit(cardPackage, attributeBO);
    }

    //public String submit(CardBO cardBO, SubmitAttributeBO attribute) {
    public String submit(EnableCardPackage enableCardPackage, SubmitAttributeBO attributeBO){
        //2.get submit action info
        ActionItem submitAction = enableCardPackage.getBody().getAction().getItems().stream().filter(e -> PPRConstants.ANSWER_CARD_ACTION_SUBMIT.equals(e.getName())).collect(Collectors.toList()).get(0);
        //1.get test info
        TestInfoBO testInfo = testInfoService.get(null, submitAction.readProperty("stepId"), submitAction.readProperty("fileId"), null, false);
        //3.get submit recipient info
        TestRecipientInfoBO recipient = RecipientCacheMap.get(testInfo.getTestId(), submitAction.readProperty("userId"));
        //4.get paper question node
        Map<String, PaperNodeInfoBO> nodesMap = this.getQuestionNode(testInfo.getExamId());
        List<PPRAnswerInfoBO> answers = this.convertAnswer(enableCardPackage.getBody().getAnswer());
        //5.do mark
        answers.forEach(answer -> {
            AutoMarkStrategyUtils.mark(answer, nodesMap.get(answer.getQuestionId()));
        });
        TestUserInfoBO testUserInfoBO = AutoMarkStrategyUtils.resetUserMarkStatus(answers);
        //6.TestUserPO
        TestUserInfoPO testUserInfoPO = buildTestUser(testInfo.getTestId(), recipient, testUserInfoBO, attributeBO, submitAction);
        //7.TestUserAnswerPO
        List<UserAnswerInfoPO> userAnswerPOs = buildUserAnswers(answers, submitAction.readProperty("userId"), testInfo.getExamId(), testUserInfoPO.getTestUserId(), testInfo.getTestId(), nodesMap);
        //8.Batch Insert Stamps And Canvas, Insert userAnswer And TestUser Info
        insertAnswerCanvas(userAnswerPOs);
        insertAnswerStamps(userAnswerPOs);
        packageUserAnswerInfoDAO.insertList(userAnswerPOs);
        //Delete previous submission information
        testUserInfoDAO.removePrevSubmit(testUserInfoPO.getTestId(), testUserInfoPO.getUserId());
        testUserInfoDAO.insertSelective(testUserInfoPO);
        //9.add Error Question Record

        //10. Send Message Notice StepTask Update score
        if (testUserInfoPO.getMarkStatus().equals(AutoMarkStrategyUtils.MARK_STATUS_MARKED)) {
            //testUserInfoService.notifyStepTaskMarkResult(testInfo.getTestId(), testInfo.getStepId(), testInfo.getActivityType(), testUserInfoPO.getUserId(), testUserInfoPO.getTestUserId(), testUserInfoPO.getUserScore());
            this.sendMarkInfo(testUserInfoPO.getTestUserId(), testUserInfoPO.getUserScore(), testUserInfoPO.getUserId());

        }
        return testUserInfoPO.getTestUserId();
    }

    private void sendMarkInfo(String testUserId, Float score, String userId) {
        AddStepRecordListDTO addStepRecordListDTO = new AddStepRecordListDTO();
        addStepRecordListDTO.setAddStepRecordList(new ArrayList<>());

        AddStepRecordDTO stepRecordDTO = new AddStepRecordDTO();
        stepRecordDTO.setStepInstanceId(testUserId);
        stepRecordDTO.setScore(Float.toString(score));
        stepRecordDTO.setUserId(userId);

        addStepRecordListDTO.getAddStepRecordList().add(stepRecordDTO);
        try {
            iStepRecordV2ServiceSDK.mark(addStepRecordListDTO);
        }catch (Exception e) {
            LOGGER.error("Auto Mark notify Step Task Error, StepInstanceId=" + testUserId + ", score=" + score + ", userId=" + userId, e);
            throw new MicroServiceException("AnswerInfoService-submit-001", "Auto Mark notify Step Task Error, StepInstanceId=" + testUserId + ", score=" + score + ", userId=" + userId);
        }
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
    private TestUserInfoPO buildTestUser(String testId, TestRecipientInfoBO recipient, TestUserInfoBO testUser, SubmitAttributeBO attributeBO, ActionItem submitAction) {
        Date today = Calendar.getInstance().getTime();
        TestUserInfoPO testUserPO = new TestUserInfoPO();
        testUserPO.setTestUserId(attributeBO.getTestUserId());
        testUserPO.setActivityId(submitAction.readProperty("stepId"));
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
            testUserPO.setStartAnswerTime(new Date(submitAction.readProperty("startTimestamp")));
            testUserPO.setEndAnswerTime(new Date(submitAction.readProperty("endTimestamp")));
            testUserPO.setAnswerCostTime(Long.valueOf(submitAction.readProperty("endTimestamp")) - Long.valueOf(submitAction.readProperty("startTimestamp")));
        }catch (Exception e){
            testUserPO.setAnswerCostTime(0L);
            testUserPO.setStartAnswerTime(today);
            testUserPO.setEndAnswerTime(today);
        }
        try{
            testUserPO.setSubmitTime(new Date(submitAction.readProperty("submitTimestamp")));
        }catch (Exception e){
            testUserPO.setSubmitTime(new Date());
        }
        testUserPO.setUserScore(testUser.getUserScore());
        testUserPO.setSubmitStatus("1");
        testUserPO.setMarkStatus(testUser.getMarkStatus());
        testUserPO.setCreator(testUserPO.getUserId());
        testUserPO.setUpdator(testUserPO.getUserId());
        testUserPO.setDelStatus(0);
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
            userAnswer.setTestUserId(testUserId).setUserId(userId).setTestId(testId).setExamId(paperId).setQuestionId(answer.getQuestionId())
                .setUserAnswer(answer.getAnswer()).setAnswerScore(answer.getAnswerScore()).setAnswerStatus(answer.getAnswerStatus());
            try{
                long answerCostTime = 0L;
                List<AnswerTrailBO> trails = answer.getTrails();
                List<UserAnswerStampInfoPO> answerStamps = new ArrayList<>();
                for (AnswerTrailBO trail : trails) {
                    answerCostTime += trail.getEnd() - trail.getStart();
                    UserAnswerStampInfoPO stamp = new UserAnswerStampInfoPO();
                    stamp.setAnswerStampId(tokenGenerator.getToken().toString());
                    stamp.setAnswerId(userAnswer.getAnswerId()).setBeginTime(new Date(trail.getStart())).setEndTime(new Date(trail.getEnd()))
                        .setLastTime((trail.getEnd() - trail.getStart())/1000).setQuestionId(answer.getQuestionId()).setTestId(testId).setExamId(paperId)
                        .setCreator(userId).setCreateTime(today).setUpdator(userId).setUpdateTime(today);
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
                    canvasPO.setCanvasId(tokenGenerator.getToken().toString()).setAnswerId(userAnswer.getAnswerId()).setCanvasType("0")
                        .setCanvasOrder(order).setCreator(userId).setCreateTime(today).setUpdator(userId).setUpdateTime(today).setFileId(canvas.getFileId())
                        .setFileName(canvas.getFileName()).setUrl(canvas.getUrl());
                    canvases.add(canvasPO);
                    canvasPO = new UserAnswerCanvasInfoPO();
                    canvasPO.setCanvasId(tokenGenerator.getToken().toString()).setAnswerId(userAnswer.getAnswerId()).setCanvasType("1")
                        .setCanvasOrder(order).setCreator(userId).setCreateTime(today).setUpdator(userId).setUpdateTime(today).setFileId(canvas.getFileId())
                        .setFileName(canvas.getFileName()).setUrl(canvas.getUrl());
                    canvases.add(canvasPO);
                }
                userAnswer.setCanvases(canvases);
            }
            userAnswer.setMarkStatus(answer.getMarkStatus()).setQuestionScore(questionNodes.get(answer.getQuestionId()).getPoints())
                .setCreator(userId).setCreateTime(today).setUpdator(userId).setUpdateTime(today);
            userAnswers.add(userAnswer);
        }
        return userAnswers;
    }
}
