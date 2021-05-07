package com.enableets.edu.pakage.framework.ppr.test.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.test.dao.ActivityAssignerDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.QuestionAssignmentDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.QuestionAssignmentRecipientDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestRecipientInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestUserInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.dao.PackageUserAnswerInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.ActivityAssignerPO;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionAssignmentPO;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionAssignmentRecipientPO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestRecipientInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.po.UserAnswerInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.AutoMarkStrategyUtils;
import com.enableets.edu.pakage.framework.ppr.bo.ActivityAssignerBO;
import com.enableets.edu.pakage.framework.ppr.bo.ActivityTeacherBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentMarkedProcessBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentRecipientBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentTeacherMarkedProcessBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;
import com.enableets.edu.pakage.framework.ppr.core.tablecopy.TableCopyComponent;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/
@Service
public class TestInfoService {
    
    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private TestPaperService testPaperService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TestInfoDAO testInfoDAO;

    @Autowired
    private TestUserInfoDAO testUserInfoDAO;

    @Autowired
    private PackageUserAnswerInfoDAO packageUserAnswerInfoDAO;

    @Autowired
    private TestRecipientInfoDAO testRecipientInfoDAO;

    @Autowired
    private QuestionAssignmentDAO questionAssignmentDAO;

    @Autowired
    private QuestionAssignmentRecipientDAO questionAssignmentRecipientDAO;

    @Autowired
    private ActivityAssignerDAO activityAssignerDAO;

    @Autowired
    private ITokenGenerator iTokenGenerator;

    /**
     * Query test information
     * @param testId Test ID
     * @param stepId Step ID
     * @param fileId File ID
     * @param examId Exam ID
     * @return Test Info
     */
    public TestInfoBO get(String testId, String stepId, String fileId, String examId, Boolean includeRecipients){
        if (StringUtils.isBlank(testId)){
            if (StringUtils.isBlank(stepId)) return null;
            else if (StringUtils.isBlank(fileId) && StringUtils.isBlank(examId)) return null;
        }
        if (includeRecipients == null) includeRecipients = Boolean.FALSE;
        String testCacheKey = getTestCacheKey(testId, stepId, fileId, examId);
        if (StringUtils.isBlank(testCacheKey)) return null;
        String testStr = stringRedisTemplate.opsForValue().get(testCacheKey);
        TestInfoBO testInfo = null;
        if (StringUtils.isNotBlank(testStr)) {
            testInfo = JsonUtils.convert(testStr, TestInfoBO.class);
        }else{
            TestInfoPO testInfoPO = testInfoDAO.get(testId, stepId, fileId, examId);
            if (testInfoPO == null) return null;
            testInfo = BeanUtils.convert(testInfoPO, TestInfoBO.class);
            stringRedisTemplate.opsForValue().set(new StringBuilder(PPRConstants.TEST_CACHE_KEY_PREFIX).append(testInfoPO.getTestId()).toString(), JsonUtils.convert(testInfo), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(new StringBuilder(PPRConstants.TEST_CACHE_KEY_PREFIX).append(testInfoPO.getActivityId()).append("_").append(testInfoPO.getFileId()).toString(), JsonUtils.convert(testInfo), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            stringRedisTemplate.opsForValue().set(new StringBuilder(PPRConstants.TEST_CACHE_KEY_PREFIX).append(testInfoPO.getActivityId()).append("_").append(testInfoPO.getExamId()).toString(), JsonUtils.convert(testInfo), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
        }
        if (testInfo != null && includeRecipients) {
            testInfo.setRecipients(RecipientCacheMap.get(testInfo.getTestId()));
        }
        return testInfo;
    }

    /**
     * GET Test Recipients
     * @param testId TEST ID
     * @return
     */
    public List<TestRecipientInfoBO> getRecipients(String testId){
        String recipientStr = stringRedisTemplate.opsForValue().get(new StringBuilder(PPRConstants.TEST_RECIPIENT_CACHE_KEY_PREFIX).append(testId).toString());
        if (StringUtils.isNotBlank(recipientStr)){
            return JsonUtils.convert2List(recipientStr, TestRecipientInfoBO.class);
        }else{
            List<TestRecipientInfoPO> recipients = testRecipientInfoDAO.get(testId, null);
            List<TestRecipientInfoBO> recipientBOs = BeanUtils.convert(recipients, TestRecipientInfoBO.class);
            if (CollectionUtils.isNotEmpty(recipientBOs)){
                stringRedisTemplate.opsForValue().set(new StringBuilder(PPRConstants.TEST_RECIPIENT_CACHE_KEY_PREFIX).append(testId).toString(), JsonUtils.convert(recipientBOs), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            }
            return recipientBOs;
        }
    }

    /**
     * Create a new Test
     * @param testInfoBO
     * @return
     */
    public TestInfoBO add(TestInfoBO testInfoBO){
        Assert.isTrue(com.enableets.edu.framework.core.util.StringUtils.isNotBlank(testInfoBO.getExamId()) || com.enableets.edu.framework.core.util.StringUtils.isNotBlank(testInfoBO.getFileId()), "'fileId' And 'examId' cannot be null at the same time!");
        if (StringUtils.isBlank(testInfoBO.getExamId())){
            String examId = this.getExamIdByFileId(testInfoBO.getFileId());
            if (StringUtils.isBlank(examId)) {
                throw new MicroServiceException("38-", "Paper Not exsit!");
            }
            testInfoBO.setExamId(examId);
        }
        PaperInfoBO paperInfoBO = testPaperService.get(testInfoBO.getExamId());
        testInfoBO.setExamName(paperInfoBO.getName());
        TestInfoBO test = this.get(null, testInfoBO.getActivityId(), testInfoBO.getFileId(), testInfoBO.getExamId(), false);
        if (test != null) {
            throw new MicroServiceException("38-", "Test Info exist!");
        }
        testInfoBO.setTestId(iTokenGenerator.getToken().toString());
        //1、Copy Paper Info From DB paper_storage To Paper_microservice
        try {
            TableCopyComponent paperCopyService = SpringBeanUtils.getBean("paperCopyService");
            Map<String, Object> condition = new HashMap<>();
            condition.put("examId", testInfoBO.getExamId());
            paperCopyService.copyTable(condition);
        } catch (IOException e) {
            throw new MicroServiceException("38-", "Copy Data From DB paper_storage To paper_microservice Error!");
        }
        //2.
        if (CollectionUtils.isNotEmpty(testInfoBO.getRecipients())) {
            List<TestRecipientInfoPO> recipients = new ArrayList<TestRecipientInfoPO>();
            for (TestRecipientInfoBO recipient : testInfoBO.getRecipients()) {
                TestRecipientInfoPO recipientPO = BeanUtils.convert(recipient, TestRecipientInfoPO.class);
                if (com.enableets.edu.framework.core.util.StringUtils.isBlank(recipientPO.getClassId())){
                    recipientPO.setClassId(recipientPO.getGroupId());
                    recipientPO.setClassName(recipientPO.getGroupName());
                }
                if (com.enableets.edu.framework.core.util.StringUtils.isBlank(recipientPO.getGroupId())){
                    recipient.setGroupId(recipientPO.getClassId());
                    recipient.setGroupName(recipientPO.getClassName());
                }
                if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(recipientPO.getClassId()) && com.enableets.edu.framework.core.util.StringUtils.isNotBlank(recipientPO.getGroupId()) && recipientPO.getClassId().equals(recipientPO.getGroupId())){
                    if (com.enableets.edu.framework.core.util.StringUtils.isBlank(recipientPO.getClassName())) recipientPO.setClassName(recipientPO.getGroupName());
                    if (com.enableets.edu.framework.core.util.StringUtils.isBlank(recipientPO.getGroupName())) recipientPO.setGroupName(recipientPO.getClassName());
                }
                recipientPO.setTestId(Long.valueOf(testInfoBO.getTestId()));
                recipientPO.setCreator(testInfoBO.getSender());
                recipientPO.setUpdator(testInfoBO.getSender());
                recipientPO.setCreateTime(new Date());
                recipientPO.setUpdateTime(new Date());
                recipients.add(recipientPO);
            }
            if (recipients.size() > 0) {
                testRecipientInfoDAO.insertList(recipients);
                //Reason for doing this：3.0 No grade options for Activity ————Max_Du
                if (com.enableets.edu.framework.core.util.StringUtils.isBlank(testInfoBO.getGradeCode())) {
                    testInfoBO.setGradeCode(recipients.get(0).getGradeCode());
                    testInfoBO.setGradeName(recipients.get(0).getGradeName());
                }
            }
            testInfoBO.setRecipients(BeanUtils.convert(recipients, TestRecipientInfoBO.class));
        }
        testInfoBO.setMarkType(StringUtils.defaultIfBlank(testInfoBO.getMarkType(), "0"));
        testInfoBO.setTestType(StringUtils.defaultIfBlank(testInfoBO.getTestType(), "2"));
        testInfoBO.setTestPublishType(StringUtils.defaultIfBlank(testInfoBO.getTestPublishType(), "0"));
        TestInfoPO testPO = BeanUtils.convert(testInfoBO, TestInfoPO.class);
        testPO.setDelStatus("0");
        testPO.setCreator(testInfoBO.getSender());
        testPO.setUpdator(testInfoBO.getSender());
        testPO.setCreateTime(new Date());
        testPO.setUpdateTime(new Date());
        testPO.setTestPublishTime(new Date());
        if (com.enableets.edu.framework.core.util.StringUtils.isNotBlank(testInfoBO.getAppId())) testPO.setAppId(testInfoBO.getAppId());
        else testPO.setAppId(PPRConstants.CLIENT_APP);
        testInfoDAO.insertSelective(testPO);
        return testInfoBO;
    }

    private String getExamIdByFileId(String fileId){
        List<ContentInfoDTO> contents = contentInfoServiceSDK.queryByFileId(fileId, Constants.CONTENT_TYPE_EXAM);
        if (CollectionUtils.isEmpty(contents)) return null;
        String examId = null;
        for (ContentInfoDTO content : contents) {
            if ((StringUtils.isNotBlank(content.getProviderContentId()) && content.getContentId().toString().equals(content.getProviderContentId())) || Constants.CONTENT_PRIVATE_TYPE.equals(content.getProviderCode())){
                examId = content.getContentId().toString(); break;
            }
        }
        return examId;
    }

    private String getTestCacheKey(String testId, String stepId, String fileId, String examId){
        StringBuilder sb = new StringBuilder(PPRConstants.TEST_CACHE_KEY_PREFIX);
        if (StringUtils.isNotBlank(testId)) return sb.append(testId).toString();
        else {
            sb.append(stepId).append("_");
            if (StringUtils.isNotBlank(fileId)) return sb.append(fileId).toString();
            if (StringUtils.isNotBlank(examId)) return sb.append(examId).toString();
        }
        return null;
    }

    /**
     * Test Complete Marked
     * @param testId
     */
    public void complete(String testId) {
        testUserInfoDAO.completeMark2(testId, null);
    }

    /**
     * Query review progress
     * @param stepId
     * @param fileId
     */
    public List<QuestionAssignmentMarkedProcessBO> queryMarkedProgress(String stepId, String fileId) {
        TestInfoBO test = this.get(null, stepId, fileId, null, false);
        if (test == null){
            throw new MicroServiceException("38- ", "Test info not found!");
        }
        List<QuestionAssignmentBO> assignments = this.queryAssign(test.getTestId(), null);
        List<UserAnswerInfoPO> userMarkQuestions = packageUserAnswerInfoDAO.queryUserMarkQuestion(test.getTestId());
        if (CollectionUtils.isNotEmpty(userMarkQuestions)){
            // map of ques - user - markStatus
            Map<String, Map<String, String>> quesUserMarkStatusMap = userMarkQuestions.stream().collect(Collectors.groupingBy(UserAnswerInfoPO::getQuestionId, Collectors.toMap(UserAnswerInfoPO::getUserId, UserAnswerInfoPO::getMarkStatus)));
            for (QuestionAssignmentBO assignment : assignments) {
                if (CollectionUtils.isEmpty(assignment.getRecipients()) || !quesUserMarkStatusMap.containsKey(assignment.getQuestionId())) {
                    continue;
                }
                // get the id of user who has answered and will be marked by current teacher
                Set<String> userIds = assignment.getRecipients().stream().map(QuestionAssignmentRecipientBO::getUserId).collect(Collectors.toSet());
                Map<String, String> userMarkStatusMap = quesUserMarkStatusMap.get(assignment.getQuestionId());
                userIds.retainAll(userMarkStatusMap.keySet());
                assignment.setSubmitCount(userIds.size());
                // filter the count which is marked
                long markCount = userMarkStatusMap.entrySet().stream()
                        .filter(entry -> userIds.contains(entry.getKey()))
                        .filter(entry -> AutoMarkStrategyUtils.MARK_STATUS_MARKED.equals(entry.getValue())).count();
                assignment.setMarkedCount((int)markCount);
            }
        }
        // 3. join the result
        Map<String, List<QuestionAssignmentBO>> questionMap = assignments.stream().collect(Collectors.groupingBy(QuestionAssignmentBO::getQuestionId));
        List<QuestionAssignmentMarkedProcessBO> result = questionMap.entrySet().stream().map(entry -> {
            List<QuestionAssignmentBO> assigns = entry.getValue();
            List<QuestionAssignmentTeacherMarkedProcessBO> assignmentProcesses = new ArrayList<>();
            int markCount = 0, submitCount = 0, totalCount = 0;
            for (QuestionAssignmentBO assignment : assigns) {
                markCount += assignment.getMarkedCount();
                submitCount += assignment.getSubmitCount();
//                totalCount += assignment.getRecipients().size();
                assignmentProcesses.add(buildTeacherMarkedProcess(assignment.getUserId(), assignment.getFullName(), assignment.getMarkedCount(), assignment.getSubmitCount()));
            }
            QuestionAssignmentMarkedProcessBO questionAssign = new QuestionAssignmentMarkedProcessBO();
            questionAssign.setQuestionId(entry.getKey());
            questionAssign.setMarkedCount(markCount);
//            questionAssign.setTotal(totalCount);
            questionAssign.setTotal(submitCount);
            questionAssign.setMarkedRatio((float) Math.round((float) questionAssign.getMarkedCount() / questionAssign.getTotal() * 10000) / 10000);
            questionAssign.setAssignmentProcesses(assignmentProcesses);
            return questionAssign;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * build teacher marked process info
     * @param userId user id
     * @param fullName full name
     * @param markedCount marked count
     * @param total total count
     * @return process info
     */
    private QuestionAssignmentTeacherMarkedProcessBO buildTeacherMarkedProcess(String userId, String fullName, int markedCount, int total) {
        QuestionAssignmentTeacherMarkedProcessBO processBO = new QuestionAssignmentTeacherMarkedProcessBO();
        processBO.setUserId(userId);
        processBO.setFullName(fullName);
        processBO.setMarkedCount(markedCount);
        processBO.setTotal(total);
        if (total != 0) {
            processBO.setMarkedRatio((float) Math.round((float) processBO.getMarkedCount() / processBO.getTotal() * 10000) / 10000);
        }
        return processBO;
    }
    /**
     * 查询指派批阅老师(同一题目包含其他老师)
     * @param testId
     * @param userId
     * @return
     */
    public List<QuestionAssignmentBO> queryAssign(String testId, String userId){
        List<QuestionAssignmentPO> list = questionAssignmentDAO.query(testId, userId);
        return BeanUtils.convert(list, QuestionAssignmentBO.class);
    }

    /**
     * 新增指定批阅的老师
     * @param assignments
     */
    public boolean addTestAssignerTeacher(List<QuestionAssignmentBO> assignments) {
        if (CollectionUtils.isEmpty(assignments)) {
            return Boolean.FALSE;
        }
        TestInfoBO testInfo = this.get(assignments.get(0).getTestId(), null, null, null,false);
        if (testInfo == null) return Boolean.FALSE;
        /*新增前删除数据*/
        Date nowDate = Calendar.getInstance().getTime();
        questionAssignmentDAO.remove(assignments.get(0).getTestId());
        List<QuestionAssignmentPO> assignmentPOS = this.transform(assignments, testInfo.getTestId());
        /*批量新增数据*/
        questionAssignmentDAO.insertList(assignmentPOS);
        List<QuestionAssignmentRecipientPO> recipients = new ArrayList<>();
        for (QuestionAssignmentPO assignmentPO : assignmentPOS) {
            if (CollectionUtils.isNotEmpty(assignmentPO.getRecipients())) recipients.addAll(assignmentPO.getRecipients());
        }
        questionAssignmentRecipientDAO.insertList(recipients);
        //新增activity_assigner
        this.addActivityAssigner(testInfo.getActivityId(), assignments);
        return true;
    }

    private void addActivityAssigner(String activityId, List<QuestionAssignmentBO> assignments){
        /*新增activity_assigner 数据*/
        ActivityAssignerBO activityAssignerBO = new ActivityAssignerBO();
        activityAssignerBO.setActivityId(activityId);
        List<ActivityTeacherBO> teachers = new ArrayList<>();
        Set<String> teacherUserIds = new HashSet<>();
        for (QuestionAssignmentBO assignmentBO : assignments) {
            if (!teacherUserIds.contains(assignmentBO.getUserId())) {
                ActivityTeacherBO teacher = new ActivityTeacherBO();
                teacher.setFullName(assignmentBO.getFullName());
                teacher.setUserId(assignmentBO.getUserId());
                teacher.setAction(PPRConstants.ACTIVITY_ASSIGNER_ACTION_QUESTION_MARK);
                teachers.add(teacher);
            }
        }
        activityAssignerBO.setTeachers(teachers);
        this.addActivityAssigner(activityAssignerBO);
    }

    @Transactional
    public Boolean addActivityAssigner(ActivityAssignerBO activityAssignerBO) {

        //删除数据库中已存在的活动信息
        activityAssignerDAO.deleteActivityAssigner(activityAssignerBO.getActivityId());

        //构建set集合去重
        Set<ActivityTeacherBO> teacherSet = new HashSet<>();
        teacherSet.addAll(activityAssignerBO.getTeachers());
        List<ActivityTeacherBO> teachers = new ArrayList<>(teacherSet);

        List<ActivityAssignerPO> activityAssigners = new ArrayList<>();
        Date time = Calendar.getInstance().getTime();
        for (ActivityTeacherBO teacher : teachers) {
            ActivityAssignerPO activityAssigner = new ActivityAssignerPO();
            activityAssigner.setActivityId(activityAssignerBO.getActivityId());
            activityAssigner.setUserId(teacher.getUserId());
            activityAssigner.setAction(teacher.getAction());
            activityAssigner.setFullName(teacher.getFullName());
            activityAssigner.setCreator(activityAssignerBO.getCreator());
            activityAssigner.setCreateTime(time);
            activityAssigner.setUpdator(activityAssignerBO.getCreator());
            activityAssigner.setUpdateTime(time);
            activityAssigners.add(activityAssigner);
        }
        //将新的活动信息存入数据库
        activityAssignerDAO.batchAdd(activityAssigners);
        return Boolean.TRUE;
    }

    private List<QuestionAssignmentPO> transform(List<QuestionAssignmentBO> assignments, String testId){
        //1、查询所有的被发布考试的学生
        List<TestRecipientInfoBO> recipients = this.getRecipients(testId);
        //2、统计每一个题目的老师数量
        Map<String, Integer> questionTeacherCountMap = new HashMap<>();
        Date time = Calendar.getInstance().getTime();
        for (QuestionAssignmentBO assignmentBO : assignments) {
            assignmentBO.setAssignmentId(iTokenGenerator.getToken().toString());
            assignmentBO.setCreateTime(time);
            assignmentBO.setUpdateTime(time);
            if (questionTeacherCountMap.get(assignmentBO.getQuestionId()) == null){
                questionTeacherCountMap.put(assignmentBO.getQuestionId(), 1);
            }else{
                questionTeacherCountMap.put(assignmentBO.getQuestionId(), questionTeacherCountMap.get(assignmentBO.getQuestionId()).intValue() + 1);
            }
        }
        for (QuestionAssignmentBO assignment : assignments) {
            assignment.setRecipients(this.getTeacherRecipient(assignment.getAssignmentId(), assignment.getSequence(), questionTeacherCountMap.get(assignment.getQuestionId()).intValue(), recipients));
        }
        return BeanUtils.convert(assignments, QuestionAssignmentPO.class);
    }

    private List<QuestionAssignmentRecipientBO> getTeacherRecipient(String assignmentId, Integer sequence, int teacherCount, List<TestRecipientInfoBO> recipients){
        if (teacherCount > recipients.size()) {  //指派老师总人数少于学生人数(应该不会出现这种情况)， 都分给第一个老师算了
            if (sequence.intValue() == 1) {
                return this.transformRecipient2AssignmentRecipient(assignmentId, recipients);
            }else return null;
        }else{
            int pageSize = recipients.size() / teacherCount;   //均分人数
            int fromIndex = (sequence.intValue() - 1) * pageSize;
            int toIndex = sequence.intValue() * pageSize;
            if (sequence.intValue() == teacherCount) {   //当时最后一个老师的时候
                toIndex = recipients.size();
            }
            return this.transformRecipient2AssignmentRecipient(assignmentId, recipients.subList(fromIndex, toIndex));
        }
    }

    private List<QuestionAssignmentRecipientBO> transformRecipient2AssignmentRecipient(String assignmentId, List<TestRecipientInfoBO> recipients){
        List<QuestionAssignmentRecipientBO> assignmentRecipients = new ArrayList<>();
        Date time = Calendar.getInstance().getTime();
        for (TestRecipientInfoBO recipient : recipients) {
            QuestionAssignmentRecipientBO assignmentRecipient = new QuestionAssignmentRecipientBO();
            assignmentRecipient.setAssignmentId(assignmentId);
            assignmentRecipient.setUserId(recipient.getUserId());
            assignmentRecipient.setFullName(recipient.getUserName());
            assignmentRecipient.setCreateTime(time);
            assignmentRecipient.setUpdateTime(time);
            assignmentRecipients.add(assignmentRecipient);
        }
        return assignmentRecipients;
    }
}
