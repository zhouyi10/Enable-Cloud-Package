package com.enableets.edu.pakage.manager.mark.service;

import cn.hutool.core.collection.CollectionUtil;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.cache.ICache;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.mark.bo.*;
import com.enableets.edu.pakage.manager.mark.core.CheckMarkingDirector;
import com.enableets.edu.pakage.manager.mark.core.RedisSupportExtendCache;
import com.enableets.edu.sdk.activity.dto.ActivityAssigner2GroupDTO;
import com.enableets.edu.sdk.activity.service.IActivityInfoService;
import com.enableets.edu.sdk.assessment.IMarkInfoService;
import com.enableets.edu.sdk.assessment.IQuestionAssignmentService;
import com.enableets.edu.sdk.assessment.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 批阅服务
 * @Author walle_yu@enable-ets.com
 * @since 2018/12/12
 */
@Service
public class MarkService {


    private static final Logger logger = LoggerFactory.getLogger(MarkService.class);

    @Value("${server.servlet.context-path}")
    private String contextPath;


    /** 评测答题信息sdk*/
    @Autowired
    private IMarkInfoService markInfoServiceSDK;


    /** 试卷信息service */
    @Autowired
    private PaperService paperService;


    /** */
    @Autowired
    private IActivityInfoService activityInfoServiceSDK;

    @Autowired
    private IQuestionAssignmentService questionAssignmentServiceSDK;

    @Autowired
    private TestInfoService testInfoService;

    /**
     * 用户信息缓存
     */
    @Resource(name="markCacheSupport")
    ICache<String> markCacheSupport;

    @Resource(name = "markTestCacheSupport")
    RedisSupportExtendCache<String> markTestCacheSupport;

    private Lock lock = new ReentrantLock();


    /** 查询试卷信息*/
    public PaperInfoBO getTestExam(String testId, String activityId, String fileId,String userId, String actionType){
        TestInfoBO test = testInfoService.get(testId, activityId, fileId);
        if (test == null){
        	logger.info("Test info doesn't exist!");
            throw new MicroServiceException("MarkService_001", "Test info is not exist");
        }
        List<TestRecipientGroupBO> list = test.getSendPaperGroups();
        List<TestRecipientGroupBO> list1 = new ArrayList<>();
        PaperInfoBO exam = paperService.getExam(test.getExamId());
        if(Constants.ASSIGNER_ACTION_TYPE.equals(actionType)) {
            StringBuffer sb = new StringBuffer().append(test.getActivityId()).append("&").append(userId).append("&").append(1);
            String assignerStr = markCacheSupport.get(sb.toString());
            if (StringUtils.isBlank(assignerStr)){
                List<ActivityAssigner2GroupDTO> assigners = activityInfoServiceSDK.getActivityAssignerInfoV2(test.getActivityId(), null, userId, "1", null, null);
                for (ActivityAssigner2GroupDTO dto : assigners) {
                    for (TestRecipientGroupBO bo : list) {
                        if(dto.getGroupId().equals(bo.getClassId()) || dto.getGroupId().equals(bo.getGroupId())){
                            list1.add(bo);
                        }
                    }
                }
                markCacheSupport.put(sb.toString(), JsonUtils.convert(list1));
            } else {
                list1 = JsonUtils.convert2List(assignerStr, TestRecipientGroupBO.class);
            }
            exam.setSendPaperGroups(list1);
        } else {
            exam.setSendPaperGroups(list);
        }
        return exam;
    }

    /**
     * 检查当前是否有人正在批阅
     * @param testId
     * @param groupId
     * @param userId
     * @return
     */
    public boolean checkMarkStatusV2(String testId, String activityId, String groupId, String userId,String loginUserId, String actionType){
        /**
         *  1, 主线缓存：testId 下 批阅的人
         *  2、副线缓存：tsetId- userId 老师批阅的对象的详情信息：
         *     信息： 可能是 班级、学生、学生的某个题目
         */
        try {
            lock.lock();
//            if (markTestCacheSupport.contains(new StringBuilder(testId).append("_").append(baseInfo.getUserId()).toString())){
//                return true;
//            }
            CheckMarkingDirector director = new CheckMarkingDirector(testId, activityId, groupId, userId, actionType, loginUserId);
            return director.check();
        }finally {
            lock.unlock();
        }
    }

    public void updateMarkStatusIntervalV2(String testId,String loginUserId, String markingCacheInfo){
        String key = new StringBuilder(testId).append("_").append(loginUserId).toString();
        String s = markTestCacheSupport.get(key);
        if (StringUtils.isNotBlank(s)) {
            markTestCacheSupport.put(key, s);
        }else if (StringUtils.isNotBlank(markingCacheInfo)){
            markTestCacheSupport.put(key, markingCacheInfo);
        }
    }

    public void cleanMarkStatusV2(String testId,String loginUserId) {
        try {
            lock.lock();
            String key = new StringBuilder(testId).append("_").append(loginUserId).toString();
            markTestCacheSupport.remove(key);
        }finally {
            lock.unlock();
        }
    }

    public String getMarkingCacheInfo(String testId,String loginUserId){
        String key = new StringBuilder(testId).append("_").append(loginUserId).toString();
        return markTestCacheSupport.get(key);
    }


    /**
     * 获取按照学生分类的答题
     * @param answers
     * @return  key：questionId, value: 每个学生这道题目的答题
     */
    private List<UserAnswerInfoBO> getStudentAnswers(List<QueryAnswerResultDTO> answers) {
        List<UserAnswerInfoBO> userAnswers = new ArrayList<UserAnswerInfoBO>();
        for (QueryAnswerResultDTO answer : answers) {
            for (QueryTAsUserAnswerDTO answerAnswer : answer.getAnswers()) {
                UserAnswerInfoBO userAnswer = new UserAnswerInfoBO();
                userAnswer.setTestUserId(answer.getTestUserId());
                userAnswer.setUserId(answer.getUserId());
                userAnswer.setUserName(answer.getUserName());
                userAnswer.setAnswerId(answerAnswer.getAnswerId());
                userAnswer.setAnswerScore(answerAnswer.getAnswerScore());
                userAnswer.setAnswerStatus(answerAnswer.getAnswerStatus());
                if (StringUtils.isNotBlank(answerAnswer.getUserAnswer())) {
                    userAnswer.setUserAnswer(answerAnswer.getUserAnswer());
                }
                userAnswer.setQuestionScore(answerAnswer.getQuestionScore());
                userAnswer.setQuestionId(answerAnswer.getQuestionId());
                userAnswer.setParentId(answerAnswer.getParentId());
                userAnswer.setMarkStatus(answerAnswer.getMarkStatus());
                if (CollectionUtils.isNotEmpty(answerAnswer.getCanvases())){
                    List<AnswerCanvasBO> canvases = BeanUtils.convert(answerAnswer.getCanvases(), AnswerCanvasBO.class);
                    userAnswer.setCanvases(canvases);
                }
                userAnswers.add(userAnswer);
            }
        }
        return userAnswers;
    }

    /**
     * 提交批阅
     * @param markAnswerInfo
     */
    public void mark(MarkAnswerInfoBO markAnswerInfo) {
        if (StringUtils.isNotBlank(markAnswerInfo.getType()) && markAnswerInfo.getType().equals(Constants.MARK_TYPE_COMPLETE)){ //点击批阅完成，所有人都批阅完成
            for (UserAnswerInfoBO answer : markAnswerInfo.getAnswers()) {
                answer.setMarkStatus(Constants.MARK_TYPE_COMPLETE);
            }
        }
        markInfoServiceSDK.mark(BeanUtils.convert(markAnswerInfo, MarkInfoDTO.class));
//        try {
//            new Thread(() -> {
//                reportDataV2Service.refreshTestClassReportData(markAnswerInfo.getTestId());
//            }).start();
//        }catch (Exception e){
//            logger.error("clear cache[testId="+ markAnswerInfo.getTestId() +"] error!", e);
//        }
    }

	/**
	 * 判断测验是否结束
	 * @param testId 测验标识
	 */
	public boolean isTestEnd(String testId) {
        TestInfoBO testDTO = testInfoService.get(testId, null, null);
		if (testDTO != null  && testDTO.getEndTime() != null) {
			//考试时间还未结束，无法看报表。。。
			long endTime = testDTO.getEndTime().getTime();
			long currTime = Calendar.getInstance().getTimeInMillis();
			if (currTime < endTime){
				return false;
			}
			return true;
		}
		return false;
	}

    /**
     * 查询答题信息
     * @param testId 考试标识
     * @return
     */
    public Map<String, Object> queryAnswersByQuestionAssignment(String testId, String userId, String groupId, String loginUserId, PaperInfoBO exam){
        MarkAnswerInfoBO markAnswerInfo = new MarkAnswerInfoBO();
        TestInfoBO test =  testInfoService.get(testId, null, null);
        if (test == null){
            logger.info("Test info doesn't exist!");
            throw new MicroServiceException("MarkService_001", "Test info is not exist");
        }
        markAnswerInfo.setTotalCount(CollectionUtils.isEmpty(test.getRecipients()) ? 0 : test.getRecipients().size());
        markAnswerInfo.setTestId(test.getTestId());
        markAnswerInfo.setTestName(test.getTestName());
        markAnswerInfo.setExamId(test.getExamId());
        String questionIds = null;
        List<QueryQuestionAssignmentResultDTO> questionAssignments = questionAssignmentServiceSDK.get(testId, loginUserId);
        if (CollectionUtils.isNotEmpty(questionAssignments)){
            questionIds = questionAssignments.stream().filter(e -> e.getUserId().equals(loginUserId)).map(e -> e.getQuestionId()).reduce((x, y) -> x + "," + y).get();
        } else {
            throw new MicroServiceException("", "questionAssignments is Null!");
        }
        // 查询这些题目的所有大题信息
        QueryAnswerV2DTO answerV2DTO = new QueryAnswerV2DTO();
        answerV2DTO.setTestId(testId);
        answerV2DTO.setUserId(userId);
        answerV2DTO.setGroupId(groupId);
        answerV2DTO.setQuestionIds(questionIds);
        List<QueryAnswerResultDTO> answers = markInfoServiceSDK.queryAnswerV2(answerV2DTO);
        List<UserAnswerInfoBO> studentAnswers = this.getStudentAnswers(answers);
        if (StringUtils.isNotBlank(userId) || CollectionUtils.isEmpty(questionAssignments)){
            markAnswerInfo.setAnswers(studentAnswers);
        }else{
            this.filterMyMarkQuestions(markAnswerInfo, studentAnswers, questionAssignments); // 过滤答案 并设置总人数
        }
        // 3 过滤试卷题目
        PaperInfoBO paperInfoBO = filterMyMarkQuestions(questionIds, exam);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("exam", paperInfoBO);
        resultMap.put("markAnswerInfo", markAnswerInfo);
        return resultMap;
    }

    private PaperInfoBO filterMyMarkQuestions(String questionIds, PaperInfoBO exam) {
        if (exam == null || CollectionUtil.isEmpty(exam.getNodes()) || StringUtils.isBlank(questionIds)) {
            return null;
        }
        PaperInfoBO cloneExam = BeanUtils.convert(exam, PaperInfoBO.class);
        int removeCount = 0;
        for (int i = 0; i < exam.getNodes().size(); i++) {
            PaperNodeInfoBO paperNodeInfoBO = exam.getNodes().get(i);
            if (paperNodeInfoBO.getLevel() == 3 && !questionIds.contains(paperNodeInfoBO.getQuestion().getQuestionId())) {
                int cloneExamIndex = i - removeCount;
                // 如果上一个节点是大题且下一个节点也是大题, 说明大题下无题目了, 把上一个大题也删除掉.
                if ((cloneExam.getNodes().size() ==  cloneExamIndex+1 && cloneExam.getNodes().get(cloneExamIndex -  1).getLevel() == 2)|| // 最后一题
                        cloneExam.getNodes().get(cloneExamIndex-1).getLevel() == 2 && cloneExam.getNodes().get(cloneExamIndex+1).getLevel() == 2) {
                    cloneExam.getNodes().remove(cloneExamIndex - 1);
                    cloneExamIndex --;
                    removeCount++;
                }
                cloneExam.getNodes().remove(cloneExamIndex);
                removeCount++;
            }
        }
        return cloneExam;
    }

    private void filterMyMarkQuestions(MarkAnswerInfoBO markAnswerInfo, List<UserAnswerInfoBO> answers, List<QueryQuestionAssignmentResultDTO> questionAssignments){
        List<UserAnswerInfoBO> filterAnswers = new ArrayList<>();
        Map<String, Boolean> userMap = new HashMap<>();
        for (QueryQuestionAssignmentResultDTO questionAssignment : questionAssignments) {
            if (CollectionUtils.isEmpty(questionAssignment.getRecipients())) continue;
            for (QueryQuestionAssignmentRecipientResultDTO recipient : questionAssignment.getRecipients()) {
                for (UserAnswerInfoBO answer : answers) {
                    if (questionAssignment.getQuestionId().equals(answer.getParentId()) && recipient.getUserId().equals(answer.getUserId())){
                        filterAnswers.add(answer);
                        userMap.put(recipient.getUserId(), true);
                    }
                }
            }
        }
        markAnswerInfo.setAnswers(filterAnswers);
        markAnswerInfo.setSubmitCount(userMap.size());
    }

}
