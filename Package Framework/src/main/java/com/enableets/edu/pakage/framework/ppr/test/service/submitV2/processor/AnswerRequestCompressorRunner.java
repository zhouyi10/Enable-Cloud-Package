package com.enableets.edu.pakage.framework.ppr.test.service.submitV2.processor;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.framework.core.IJedisCache;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.test.bo.TAsTestRecipientBO;
import com.enableets.edu.pakage.framework.ppr.test.core.AnswerDataProcessorConfig;
import com.enableets.edu.pakage.framework.ppr.test.core.TestConstants;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerRequestDataService;
import com.enableets.edu.pakage.framework.ppr.test.service.TestActionService;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo.AnswerRequestDataBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo.SubmitAnswerRequest;
import com.enableets.edu.pakage.framework.ppr.utils.XmlUtils;
import com.enableets.edu.pakage.framework.ppr.utils.ZipUtils;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


/**
 * @author tony_liu@enable-ets.com
 * @since 2021/1/28
 **/

@Component
@Slf4j
public class AnswerRequestCompressorRunner implements ApplicationRunner {

    private static final Logger SUBMIT_ERROR_LOG = LoggerFactory.getLogger(TestConstants.SUBMIT_ERROR_LOG);

    private static final SimpleDateFormat sdf = new SimpleDateFormat(TestConstants.DATE_FORMAT_TYPE);

    /** 合并请求定时调度executorService */
    private ScheduledExecutorService scheduledExecutorService;

    /** 请求队列 */
    private static LinkedBlockingQueue<SubmitAnswerRequest> requestQueue = new LinkedBlockingQueue<>();

    /** 合并请求单次处理数量 */
    private static final int FRAME_SIZE = 100;

    @Autowired
    private AnswerRequestDataService answerRequestDataService;

    @Autowired
    private ITokenGenerator tokenGenerator;

    @Autowired
    @Qualifier("testJedisCacheSupport")
    public IJedisCache<String> testCacheSupport;

    @Autowired
    @Qualifier("submitAnswerJedisCache")
    public IJedisCache<String> submitAnswerCacheSupport;

    @Autowired
    @Qualifier("tesRecipientSubmitInfoJedisCache")
    public IJedisCache<String> tesRecipientSubmitInfoCacheSupport;

    @Autowired
    @Qualifier("saveAnswerJedisCache")
    public IJedisCache<String> saveAnswerJedisCache;

    @Autowired
    private AnswerDataProcessorConfig answerDataProcessorConfig;

    @Autowired
    private TestInfoService testInfoService;

//    @Autowired
//    private TestActionService testActionService;

    @Autowired
    private TestInfoDAO tAsTestDAO;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        scheduledExecutorService = Executors.newScheduledThreadPool(answerDataProcessorConfig.getCorePoolSize());
        this.compress();
    }

    public void compress() {
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    int size = requestQueue.size();
                    if(size == 0) return;
                    Map<String, SubmitAnswerRequest> requests = new HashMap<>();
                    int requestSize = FRAME_SIZE;
                    if(size < FRAME_SIZE) {
                        requestSize = size;
                    }
                    for(int i = 0;i < requestSize; i++){
                        SubmitAnswerRequest request = requestQueue.poll();
                        requests.put(request.getId(), request);
                    }
                    if (requests.size() > 0) {
                        doTask(requests);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        },0,100, TimeUnit.MILLISECONDS);
    }

    private void doTask(Map<String, SubmitAnswerRequest> requestMap) {
        if (requestMap == null || requestMap.size() == 0) return;
        List<AnswerRequestDataBO> list = new ArrayList<>();
        for (SubmitAnswerRequest submitAnswerRequest : requestMap.values()) {
            list.add(buildAnswerRequestDataBO(submitAnswerRequest));
        }
        batchProcess(list);
        for (AnswerRequestDataBO answerRequestDataBO : list) {
            // 放入缓存 待RabbitMQ交卷时使用
            saveAnswerJedisCache.put(answerRequestDataBO.getAnswerRequestId(), JsonUtils.convert(answerRequestDataBO));
            SubmitAnswerRequest request = requestMap.remove(answerRequestDataBO.getAnswerRequestId());
            request.getFuture().complete(answerRequestDataBO);
        }
    }

    public AnswerRequestDataBO submitAnswer(String answerCardXml) {
        try {
            //SubmitAnswerRequest submitAnswerRequest = analysisValidateSubmitAnswerInfo(answerCardXml);
            SubmitAnswerRequest submitAnswerRequest = analysisValidateAnswerCardInfo(answerCardXml);
            requestQueue.add(submitAnswerRequest);
            return submitAnswerRequest.getFuture().get();
        } catch (InterruptedException e) {
            throw new MicroServiceException(TestConstants.ERROR_CODE_THREAD_POOL_ERROR, TestConstants.ERROR_MESSAGE_THREAD_POOL_ERROR);
        } catch (ExecutionException e) {
            Throwable getCause = e.getCause().getCause() == null ? e.getCause() : e.getCause().getCause();
            if (getCause instanceof MicroServiceException) {
                throw (MicroServiceException) getCause;
            }
            throw new MicroServiceException(TestConstants.ERROR_CODE_THREAD_POOL_ERROR, TestConstants.ERROR_MESSAGE_THREAD_POOL_ERROR);
        }
    }

    public SubmitAnswerRequest analysisValidateAnswerCardInfo(String answerCardXml){
        PPRPackageLifecycle lifecycle = new PPRPackageLifecycle();
        EnableCardPackage cardPackage = null;
        try {
            cardPackage = lifecycle.parse(answerCardXml);
        }catch (Exception e) {
            SUBMIT_ERROR_LOG.error("answerCardXml format error; answerCardXml ="  + answerCardXml);
            new MicroServiceException("AnswerCardValid-001", "The AnswerCard wrong format");
        }
        if (cardPackage.getBody() == null || cardPackage.getBody().getAction() == null || CollectionUtils.isEmpty(cardPackage.getBody().getAction().getItems())) {
            SUBMIT_ERROR_LOG.error("Answer Card Action missing！");
            new MicroServiceException("AnswerCardValid-002", "Answer Card Action missing！");
        }
        ActionItem submitAction = null;
        for (ActionItem action : cardPackage.getBody().getAction().getItems()) {
            if (StringUtils.isNotBlank(action.getName()) && action.getName().equals(PPRConstants.ANSWER_CARD_ACTION_SUBMIT)){
                submitAction = action; break;
            }
        }
        if (submitAction == null) {
            SUBMIT_ERROR_LOG.error("Answer Card Submit Action missing( action name must be 'submit')！");
            new MicroServiceException("AnswerCardValid-002", "Answer Card Submit Action missing( action name must be 'submit')！");
        }
        String stepId = submitAction.readProperty("stepId");
        String fileId = submitAction.readProperty("fileId");
        String testId = submitAction.readProperty("testId");
        String userId = submitAction.readProperty("userId");
        String endTimestamp = submitAction.readProperty("endTimestamp");
        if (StringUtils.isEmpty(testId) && (StringUtils.isEmpty(stepId) || StringUtils.isEmpty(fileId))) {
            SUBMIT_ERROR_LOG.error("The AnswerCard miss testId or not stepId&fileId property; Submit Time(" + sdf.format(new Date()) + "); answerCardXml: " + answerCardXml);
            throw new MicroServiceException("AnswerCardValid-003", "The answerXml is not testId or not stepId&fileId property");
        }
        if (StringUtils.isEmpty(userId)) {
            SUBMIT_ERROR_LOG.error("The AnswerCard miss userId property; Submit Time(" + sdf.format(new Date()) + "); answerCardXml: " + answerCardXml);
            throw new MicroServiceException("AnswerCardValid-004", "The answerXml is not userId property");
        }
        if (StringUtils.isEmpty(submitAction.readProperty("startTimestamp")) || StringUtils.isEmpty(endTimestamp) || StringUtils.isEmpty(submitAction.readProperty("submitTimestamp"))) {
            SUBMIT_ERROR_LOG.error("The answerXml miss timestamp(answer or submit timestamp) property; Submit Time(" + sdf.format(new Date()) + "); answerCardXml: " + answerCardXml);
            throw new MicroServiceException("AnswerCardValid-005", "The answerXml miss timestamp(answer or submit timestamp) property;");
        }
        if (cardPackage.getBody().getAnswer() == null || CollectionUtils.isEmpty(cardPackage.getBody().getAnswer().getAnswerItems())) {
            SUBMIT_ERROR_LOG.error("The answerXml miss answer node; Submit Time(" + sdf.format(new Date()) + "); answerCardXml: " + answerCardXml);
            throw new MicroServiceException("AnswerCardValid-006", "The answerXml miss answer node");
        }
        //List<AnswerItem> answerItems = cardPackage.getBody().getAnswer().getAnswerItems();
        // Answer Item Detail valid write next time

        TestInfoBO test = testInfoService.get(testId, stepId, fileId, null, false);
        if (test == null) {
            SUBMIT_ERROR_LOG.error("Test(testId = {}, stepId={}, fileId={}, UserId={}); Submit Time({}); Test info does not exist", testId, stepId, fileId, userId, sdf.format(new Date()));
            throw new MicroServiceException("AnswerCardValid-007", "Test info does not exist, TestId:" + testId + "; StepId:" + stepId + " FileId:" + fileId);
        }
        testId = test.getTestId();
        stepId = test.getActivityId();
        fileId = test.getFileId();
        //The test has no start
        //!testActionService.testStart(test.getTestId(), userId)
        if(test.getStartTime().getTime() > Calendar.getInstance().getTime().getTime()) {
            SUBMIT_ERROR_LOG.error("Test(testId = {}, stepId = {});User({}); Submit Time({}), The test has no begin, No submitting", testId, stepId, userId, sdf.format(new Date()));
            throw new MicroServiceException("AnswerCardValid-008", "The test has no Start, No submitting");
        }
        //test has marked
        //testActionService.isMarked(userId, test.getTestId())
        //this.getCacheMarkedStatus(testId, userId).equals("1")
        if (this.getCacheMarkedStatus(testId, userId).equals("1")) {
            SUBMIT_ERROR_LOG.error("Test(testId = {}, stepId = {});User({}); Submit Time({}), Marked, No submitting ", testId, stepId, userId, sdf.format(new Date()));
            throw new MicroServiceException("AnswerCardValid-009", "Marked, No submitting");
        }
        //test has end
        if (test.getDelaySubmit() != -1) {
            Date answerTime = null;
            try{
                answerTime = new Date(Long.valueOf(endTimestamp));
            }catch(Exception ex){
                SUBMIT_ERROR_LOG.error("Test(testId = {}, stepId = {});User({}); Submit Time({}), endAnswerTime 转换失败, 使用系统席间判断交卷超时时间", testId, stepId, userId, sdf.format(new Date()));
                answerTime = Calendar.getInstance().getTime();
            }
            if (answerTime.getTime() > test.getEndTime().getTime() + test.getDelaySubmit() * 60 * 1000) {
                SUBMIT_ERROR_LOG.error("Test(testId = {}, stepId = {});User({}); Submit Time({}), Out of Date, No submitting", testId, stepId, userId, sdf.format(new Date()));
                throw new MicroServiceException("AnswerCardValid-0010", "Out of Date, Stop submitting");
            }
        }
        //submit has out of submit times
        if (test.getResubmit().intValue() != -1) {
            int cacheSubmitCount = this.getCacheSubmitCount(testId, userId);
            if (cacheSubmitCount > test.getResubmit().intValue()) { // The 0 representative can hand it in one and the representative can hand it in 2. By analogy
                SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), submitted Time({}); Out of submitted times", testId, stepId, userId, sdf.format(new Date()), cacheSubmitCount);
                throw new MicroServiceException("AnswerCardValid-0011", "Out of submitted times, Stop submitting");
            }
        }
        this.cacheStudentAnswerInfo(testId,userId);
        this.cacheAddSubmitTimes(testId, userId);
        this.cacheRecipientSubmitInfo(testId, userId);
        // 答案数据压缩并 BASE64Encoder
        return new SubmitAnswerRequest(tokenGenerator.getToken().toString(), "", ZipUtils.gzip(answerCardXml));
    }

    private SubmitAnswerRequest analysisValidateSubmitAnswerInfo(String answerXml) {
        String testId = XmlUtils.getAnswerXmlPropertyValue(answerXml, "testId");
        String activityId = "";
        String fileId = "";
        if (StringUtils.isBlank(testId)) {
            activityId =XmlUtils.getAnswerXmlPropertyValue(answerXml, "activityId");
            fileId = XmlUtils.getAnswerXmlPropertyValue(answerXml, "fileId");
        }
        String endAnswerTime = XmlUtils.getAnswerXmlPropertyValue(answerXml, "endAnswerTime");
        String userId = XmlUtils.getAnswerXmlPropertyValue(answerXml, "userId");

        if (StringUtils.isBlank(testId) && (StringUtils.isBlank(activityId) && StringUtils.isBlank(fileId))) {
            SUBMIT_ERROR_LOG.error("The answerXml is not testId or not activityId and fileId property; Submit Time(" + sdf.format(new Date()) + "); answerXml: " + answerXml);
            throw new MicroServiceException("00000", "The answerXml is not testId or not activityId and fileId property");
        }
        if (StringUtils.isBlank(userId)) {
            SUBMIT_ERROR_LOG.error("The answerXml is not testId property; Submit Time(" + sdf.format(new Date()) + "); answerXml: " + answerXml);
            throw new MicroServiceException("00005", "The answerXml is not userId property");
        }
        TestInfoBO test = testInfoService.get(testId, activityId, fileId, null, false);
        if (test != null) {
            testId = test.getTestId();
            activityId = test.getActivityId();
            //The test has no start
            if(test.getStartTime().getTime() > Calendar.getInstance().getTime().getTime()) {
                SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), The test has no begin, No submitting", testId, activityId, userId, sdf.format(new Date()));
                throw new MicroServiceException("00004", "The test has no Start, No submitting");
            }
            //test has marked
            if (this.getCacheMarkedStatus(testId, userId).equals("1")) {
                SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), Marked, No submitting ", testId, activityId, userId, sdf.format(new Date()));
                throw new MicroServiceException("00001", "Marked, No submitting");
            }
            //test has end
            if (test.getDelaySubmit() != -1) {
                Date answerTime = null;
                try{
                    //尝试从EndAnswerTime取结束答题时间
                    if(StringUtils.isNotEmpty(endAnswerTime)){
                        answerTime = sdf.parse(endAnswerTime);
                    }else{
                        //取不到则使用系统时间判断
                        answerTime = Calendar.getInstance().getTime();
                    }
                }catch(Exception ex){
                    SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), endAnswerTime 转换失败, 使用系统席间判断交卷超时时间", testId, activityId, userId, sdf.format(new Date()));
                    answerTime = Calendar.getInstance().getTime();
                }
                if (answerTime.getTime() > test.getEndTime().getTime() + test.getDelaySubmit() * 60 * 1000) {
                    SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), Out of Date, No submitting", testId, activityId, userId, sdf.format(new Date()));
                    throw new MicroServiceException("00002", "Out of Date, No submitting");
                }
            }
            //submit has out of submit times
            if (test.getResubmit().intValue() != -1) {
                int cacheSubmitCount = this.getCacheSubmitCount(testId, userId);
                if (cacheSubmitCount > test.getResubmit().intValue()) { // The 0 representative can hand it in one and the representative can hand it in 2. By analogy
                    SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId = {});User({}); Submit Time({}), submitted Time({}); Out of submitted times", testId, activityId, userId, sdf.format(new Date()), cacheSubmitCount);
                    throw new MicroServiceException("00003", "Out of submitted times, No submitting");
                }
            }
            this.cacheStudentAnswerInfo(testId,userId);
            this.cacheAddSubmitTimes(testId, userId);
            this.cacheRecipientSubmitInfo(testId, userId);
        } else {
            SUBMIT_ERROR_LOG.error("Test(testId = {}, activityId={}, fileId={}, UserId={}); Submit Time({}); Test info does not exist", testId, activityId, fileId, userId, sdf.format(new Date()));
            throw new MicroServiceException("00005", "Test info does not exist, Testid:" + testId + "; ActivityId:" + activityId + " FileId:" + fileId);
        }
        // 答案数据压缩并 BASE64Encoder
        return new SubmitAnswerRequest(tokenGenerator.getToken().toString(), userId, ZipUtils.gzip(answerXml));
    }

    private AnswerRequestDataBO buildAnswerRequestDataBO(SubmitAnswerRequest submitAnswerRequest) {
        AnswerRequestDataBO bo = new AnswerRequestDataBO();
        bo.setAnswerRequestId(submitAnswerRequest.getId());
        bo.setOriginData(submitAnswerRequest.getData());
        bo.setCreator(submitAnswerRequest.getUserId());
        bo.setUpdator(submitAnswerRequest.getUserId());
        bo.setStatus(0);
        bo.setRetryTimes(0);
        Date date = new Date();
        bo.setCreateTime(date);
        bo.setUpdateTime(date);
        return bo;
    }

    private void batchProcess(List<AnswerRequestDataBO> list) {
        if (list == null || list.size() == 0) return;
        answerRequestDataService.batchInsert(list);
    }

    public TestInfoPO getTest(String testId, String activityId, String fileId) {
        String cacheKey = testId;
        TestInfoPO test = null;
        if (StringUtils.isBlank(cacheKey)){
            cacheKey = activityId+ "_" + fileId;
        }
        //String testStr = guavaCacheService.get(testId);
        String testStr = testCacheSupport.get(cacheKey);
        if(StringUtils.isNotBlank(testStr) && !testStr.equals("null")) {
            test = JsonUtils.convert(testStr,TestInfoPO.class);
            if (test != null) return test;
        }
        test = tAsTestDAO.get(testId, activityId, fileId, null);
        if (test != null) testCacheSupport.put(cacheKey,JsonUtils.convert(test));
        return test;
    }

    private String getCacheMarkedStatus(String testId, String userId){
        String key = new StringBuffer("MarkedList_").append(testId).toString();
        String userStrs = submitAnswerCacheSupport.get(key);
        if (StringUtils.isNotEmpty(userStrs)) {
            List<String> users = JsonUtils.convert2List(userStrs, String.class);
            if (users.contains(userId)) return "1";
        }
        return "0";
    }

    private int getCacheSubmitCount(String testId, String userId){
        String key = new StringBuffer("submitCount_").append(testId).append("&").append(userId).toString();
        String s = submitAnswerCacheSupport.get(key);
        if (StringUtils.isEmpty(s)) return 0;
        else return Integer.valueOf(s).intValue();
    }

    private void cacheStudentAnswerInfo(String testId,String userId) {
        String key = new StringBuffer("student_answer_key_").append(testId).append("&").append(userId).toString();
        String value = submitAnswerCacheSupport.get(key);
        if(StringUtils.isBlank(value)) {
            submitAnswerCacheSupport.put(key,userId);
        }
    }

    private void cacheAddSubmitTimes(String testId, String userId){
        String key = new StringBuffer("submitCount_").append(testId).append("&").append(userId).toString();
        String submitCount = submitAnswerCacheSupport.get(key);
        if (StringUtils.isEmpty(submitCount)) {
            submitAnswerCacheSupport.put(key, "1");
        }else{
            submitAnswerCacheSupport.put(key, Integer.valueOf(submitCount) + 1 + "");
        }
    }

    private void cacheRecipientSubmitInfo(String testId, String userId){
        Date submitTimeD = Calendar.getInstance().getTime();
        try {
            String submitInInoStr = tesRecipientSubmitInfoCacheSupport.get(testId);
            if (StringUtils.isBlank(submitInInoStr)) return;
            List<TAsTestRecipientBO> recipients = JsonUtils.convert2List(submitInInoStr, TAsTestRecipientBO.class);
            for (TAsTestRecipientBO recipient : recipients) {
                if (!recipient.getUserId().equals(userId)) continue;
                recipient.setStatus("0");
                recipient.setSubmitTime(submitTimeD);
            }
            tesRecipientSubmitInfoCacheSupport.put(testId, JsonUtils.convert(recipients));
        }catch (Exception e){
            SUBMIT_ERROR_LOG.error("Cache submit recipient info error!", e);
        }
    }
}
