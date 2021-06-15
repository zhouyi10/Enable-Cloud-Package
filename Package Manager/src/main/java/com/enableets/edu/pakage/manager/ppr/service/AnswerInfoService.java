package com.enableets.edu.pakage.manager.ppr.service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.*;
import com.enableets.edu.pakage.card.bo.action.ActionMapper;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.core.xstream.EntityToXmlUtils;
import com.enableets.edu.pakage.framework.core.IJedisCache;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.core.MessageUtils;
import com.enableets.edu.pakage.manager.ppr.bo.*;
import com.enableets.edu.pakage.manager.ppr.core.PPRConstants;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import com.enableets.edu.pakage.ppr.bean.PPRPackageWrapper;
import com.enableets.edu.sdk.activity.dto.QueryActivityStepInfoDTO;
import com.enableets.edu.sdk.activity.dto.QueryStepResultDTO;
import com.enableets.edu.sdk.activity.dto.QueryStepTaskResultDTO;
import com.enableets.edu.sdk.activity.service.IStepTaskService;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.pakage.ppr.dto.*;
import com.enableets.edu.sdk.pakage.ppr.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/03
 **/
@Service
public class AnswerInfoService {

    @Autowired
    private IPPRTestInfoService pprTestInfoServiceSDK;

    @Autowired
    private IPPRTestUserInfoService pprTestUserInfoServiceSDK;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IStepTaskService stepTaskServiceSDK;

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private IPPRInfoService pprInfoServiceSDK;

    @Autowired
    private IPPRAnswerInfoService pprAnswerInfoServiceSDK;

    @Autowired
    @Qualifier("submitAnswerJedisCache")
    public IJedisCache<String> submitAnswerCacheSupport;

    public PaperInfoBO get(String paperId){
        QueryPPRInfoResultDTO pprInfoResultDTO = pprInfoServiceSDK.get(paperId);
        PaperInfoBO paperInfoBO = BeanUtils.convert(pprInfoResultDTO, PaperInfoBO.class);
        if (paperInfoBO == null || CollectionUtils.isEmpty(paperInfoBO.getNodes())) return null;
        List<PaperNodeInfoBO> children = null;
        for (int i = paperInfoBO.getNodes().size() - 1; i >= 0; i--) {
            PaperNodeInfoBO node = paperInfoBO.getNodes().get(i);
            if (node.getLevel() == 4){
                if (children == null) {
                    children = new ArrayList<>();
                }
                children.add(paperInfoBO.getNodes().remove(i));
            }else if (node.getLevel() == 3){
                if (children != null) {
                    Collections.reverse(children);
                    node.setChildren(children);
                    node.setChildAmount(children.size());
                    children = null;
                } else {
                    node.setChildAmount(0);
                }
            }
        }
        return paperInfoBO;
    }

    public SubmitResultBO submit2(AnswerCardBO answerCardBO){
        //0、valid
        SubmitResultBO submitResultBO = this.validSubmit(answerCardBO);
        if (submitResultBO.getSubmitStatus().equals(SubmitResultBO.ERROR)) return submitResultBO;
        //1、Get Recipient Info
        TestRecipientInfoBO recipientInfoBO = RecipientCacheMap.get(answerCardBO.getTestId(), answerCardBO.getUserId());
        //-----------------------------------------------
        //2、Get Package ppr
        EnableCardPackage enableCardPackage = this.getEnableCardPackage(answerCardBO.getPaperId());
        //3、Get EnableCard
        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        EnableCard enableCard = new EnableCard(enableCardPackage, configuration);
        //--------------------------------------
        //3.1 EnableCard Clock-In Step Action
        //enableCard.getEnableCardPackage().getBody().setAction(this.getActions(answerCardBO.getStepId(), answerCardBO.getUserId(), recipientInfoBO.getUserName(), answerCardBO.getStartTime(), answerCardBO.getEndTime()));
        //enableCard.clockIn();
        //3.2 EnableCard save (Submit)
        enableCard.getEnableCardPackage().getBody().setAnswer(this.convertAnswer(answerCardBO));
        //enableCard.save();
        //------------------------------

        enableCard.getEnableCardPackage().getBody().setAction(this.getSubmitActions(answerCardBO.getStepId(), answerCardBO.getFileId(), answerCardBO.getUserId(), recipientInfoBO.getUserName(), answerCardBO.getStartTime(), answerCardBO.getEndTime()));
        EnableCardPackage answerCard = enableCard.getEnableCardPackage();
        String answerCardXml = EntityToXmlUtils.toXml(answerCard);
        AnswerCardSubmitInfoDTO answerCardDTO = new AnswerCardSubmitInfoDTO();
        answerCardDTO.setEnableCardXml(answerCardXml);
        try {
            String businessId = pprAnswerInfoServiceSDK.submit(answerCardDTO);
            submitResultBO.setBusinessId(businessId);
        }catch (Exception e){
            submitResultBO = SubmitResultBO.error("other", MessageUtils.getMessage("MSG_70_01_03_031"));
        }
        return submitResultBO;
    }

    private SubmitResultBO validSubmit(AnswerCardBO answerCardBO) {
        QueryTestInfoResultDTO test = pprTestInfoServiceSDK.get(answerCardBO.getTestId());
        if (test == null) {
            return SubmitResultBO.error("0001", MessageUtils.getMessage("MSG_70_01_03_001")); //No test;
        }
        if(test.getStartTime().getTime() > Calendar.getInstance().getTime().getTime()) {
            return SubmitResultBO.error("0002", MessageUtils.getMessage("MSG_70_01_03_027")); //Test No start;
        }
        if (this.getCacheMarkedStatus(answerCardBO.getTestId(), answerCardBO.getUserId()).equals("1")) {
            return SubmitResultBO.error("0003", MessageUtils.getMessage("MSG_70_01_03_028")); //Marked, Stop submit
        }
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        //test has end
        if (test.getDelaySubmit() != -1) {
            Date answerTime = null;
            try{
                answerTime = sdf.parse(answerCardBO.getEndTime());
            }catch(Exception ex){
            }
            if (answerTime.getTime() > test.getEndTime().getTime() + test.getDelaySubmit() * 60 * 1000) {
                return SubmitResultBO.error("0004", MessageUtils.getMessage("MSG_70_01_03_029")); //Out of the paper submission time
            }
        }
        //submit has out of submit times
        if (test.getResubmit().intValue() != -1) {
            int cacheSubmitCount = this.getCacheSubmitCount(test.getTestId(), answerCardBO.getUserId());
            if (cacheSubmitCount > test.getResubmit().intValue()) { // The 0 representative can hand it in one and the representative can hand it in 2. By analogy
               return SubmitResultBO.error("0005", MessageUtils.getMessage("MSG_70_01_03_030")); //Out of Submit Times;
            }
        }
        return SubmitResultBO.success("");
    }

    private String getCacheMarkedStatus(String testId, String userId){
        String key = new StringBuffer("MarkedList_").append(testId).toString();
        String userStrs = submitAnswerCacheSupport.get(key);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(userStrs)) {
            List<String> users = JsonUtils.convert2List(userStrs, String.class);
            if (users.contains(userId)) return "1";
        }
        return "0";
    }

    private int getCacheSubmitCount(String testId, String userId){
        String key = new StringBuffer("submitCount_").append(testId).append("&").append(userId).toString();
        String s = submitAnswerCacheSupport.get(key);
        if (org.apache.commons.lang.StringUtils.isEmpty(s)) return 0;
        else return Integer.valueOf(s).intValue();
    }

    private Action getSubmitActions(String stepId, String fileId, String userId, String fullName, String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        List<ActionItem> items = new ArrayList<>();
        List<Item> props = new ArrayList<>();
        props.add(new Item("stepId", stepId));
        props.add(new Item("fileId", fileId));
        props.add(new Item("userId", userId));
        props.add(new Item("fullName", fullName));
        try {
            props.add(new Item("startTimestamp", String.valueOf(sdf.parse(startTime).getTime())));
            props.add(new Item("endTimestamp", String.valueOf(sdf.parse(endTime).getTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        props.add(new Item("submitTimestamp", String.valueOf(Calendar.getInstance().getTime().getTime())));
        items.add(new ActionItem("1", null, "submit", "submit", new Property(props)));
        return new Action(items);
    }

    private Action getActions(String stepId, String userId, String fullName, String startTime, String endTime) {
        List<ActionItem> items = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        Long start = null;
        try {
            start = sdf.parse(startTime).getTime();
        } catch (ParseException e) {
            start = new Date().getTime();
        }
        Long end = null;
        try {
            end = sdf.parse(endTime).getTime();
        } catch (ParseException e) {
            end = new Date().getTime();
        }
        long timestamp = Calendar.getInstance().getTime().getTime();
        QueryActivityStepInfoDTO stepInfo = stepTaskServiceSDK.queryActivityStepInfo(stepId);
        if (stepInfo == null) return null;
        QueryStepTaskResultDTO activityInfo = stepTaskServiceSDK.get(stepInfo.getActivityId(), Boolean.TRUE, Boolean.FALSE);
        if (activityInfo == null && CollectionUtils.isEmpty(activityInfo.getSteps())) return null;
        for (QueryStepResultDTO step : activityInfo.getSteps()) {
            if (PPRConstants.STEP_ACTION_RECEIVE_ACTIVITY_PAPER_TYPE.equals(step.getType()) || PPRConstants.STEP_ACTION_SUBMIT_PAPER_TYPE.equals(step.getType()) || PPRConstants.STEP_ACTION_RECEIVE_ANSWER_TYPE.equals(step.getType())){
                List<Item> props = new ArrayList<>();
                props.add(new Item("userId", userId));
                props.add(new Item("fullName", fullName));
                props.add(new Item("timestamp", timestamp + ""));
                items.add(new ActionItem(step.getStepId(), step.getType(), ActionMapper.getActionName(step.getType()), ActionMapper.getActionName(step.getType()), new Property(props)));
                continue;
            }
            if (PPRConstants.STEP_ACTION_ANSWER_PAPER_TYPE.equals(step.getType())){
                List<Item> props = new ArrayList<>();
                props.add(new Item("userId", userId));
                props.add(new Item("fullName", fullName));
                props.add(new Item("startTimestamp", start + ""));
                props.add(new Item("endTimestamp", end + ""));
                items.add(new ActionItem(step.getStepId(), step.getType(), ActionMapper.getActionName(step.getType()), ActionMapper.getActionName(step.getType()), new Property(props)));
                continue;
            }
        }
        return new Action(items);
    }

    private Answer convertAnswer(AnswerCardBO answerCardBO) {
        List<AnswerItem> answerItems = new ArrayList<>();
        Map<String, List<AnswerCardBO.AnswerInfoBO>> answerMap = answerCardBO.getAnswers().stream().map(e -> {
            if (StringUtils.isBlank(e.getParentId())) e.setParentId(e.getQuestionId());
            return e;
        }).collect(Collectors.groupingBy(AnswerCardBO.AnswerInfoBO::getParentId));
        for (Map.Entry<String, List<AnswerCardBO.AnswerInfoBO>> entry : answerMap.entrySet()) {
            List<AnswerQuestion> answerQuestions = new ArrayList<>();
            for (AnswerCardBO.AnswerInfoBO answerInfoBO : entry.getValue()) {
                AnswerQuestion aq = new AnswerQuestion();
                aq.setId(answerInfoBO.getQuestionId());
                aq.setText(answerInfoBO.getUserAnswer());
                if (CollectionUtils.isNotEmpty(answerInfoBO.getCanvases())) {
                    List<FileItem> files = new ArrayList<>();
                    for (AnswerCardBO.AnswerCanvas canvas : answerInfoBO.getCanvases()) {
                        FileItem item = new FileItem();
                        item.setFormat(canvas.getFileExt());
                        item.setFileId(canvas.getFileId());
                        item.setFileName(canvas.getFileName());
                        item.setUrl(canvas.getUrl());
                        files.add(item);
                    }
                    aq.setFiles(files);
                }
                List<Timestamp> timestamps = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(answerInfoBO.getAnswerStamp())) {
                    for (String stampStr : answerInfoBO.getAnswerStamp()) {
                        String[] split = stampStr.split(":");
                        timestamps.add(new Timestamp(Long.valueOf(split[0]), Long.valueOf(split[0]) + Long.valueOf(split[1])));
                    }
                    aq.setTrail(new Trail(timestamps));
                }
                answerQuestions.add(aq);
            }
            answerItems.add(new AnswerItem(entry.getKey(), answerQuestions));
        }
        return new Answer(answerItems);
    }

    public EnableCardPackage getEnableCardPackage(String paperId){
        if (StringUtils.isBlank(paperId)) return null;
        String cacheKey = new StringBuilder(PPRConstants.PACKAGE_PPR_CACHE_KEY_PREFIX).append("enable:card:").append(paperId).toString();
        String enableCardStr = stringRedisTemplate.opsForValue().get(cacheKey);
        EnableCardPackage enableCardPackage = null;
        if (StringUtils.isNotBlank(enableCardStr)){
            enableCardPackage = JsonUtils.convert(enableCardStr, EnableCardPackage.class);
        }else{
            PackageFileInfo packageFile = this.getPPRDocFromContentV2(paperId);
            Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
            PPRPackageWrapper pprPackageWrapper = new PPRPackageWrapper(configuration, packageFile);
            PPRPackageLifecycle lifecycle = new PPRPackageLifecycle(pprPackageWrapper);
            lifecycle.parse();
            enableCardPackage = pprPackageWrapper.getEnableCard().getEnableCardPackage();
            if (enableCardPackage != null){
                stringRedisTemplate.opsForValue().set(cacheKey, JsonUtils.convert(enableCardPackage), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        }
        return enableCardPackage;
    }

    private PackageFileInfo getPPRDocFromContentV2(String paperId){
        ContentInfoDTO content = contentInfoServiceSDK.get(Long.valueOf(paperId)).getData();
        if (content != null && CollectionUtils.isNotEmpty(content.getFileList())){
            for (ContentFileInfoDTO file : content.getFileList()) {
                if (file.getFileExt().equals("ppr")) {
                    PackageFileInfo ppr = new PackageFileInfo();
                    ppr.setFileId(file.getFileId());
                    ppr.setName(file.getFileName());
                    ppr.setDownloadUrl(file.getUrl());
                    ppr.setMd5(file.getMd5());
                    ppr.setSize(file.getSize());
                    ppr.setSizeDisplay(file.getSizeDisplay());
                    ppr.setExt(file.getFileExt());
                    return ppr;
                }
            }
        }
        return null;
    }

    public MarkActionInfoBO queryAnswer(String testId, String userId, String groupIds) {
        MarkActionInfoBO markAnswerInfo = new MarkActionInfoBO();
        QueryTestInfoResultDTO testInfoBO = pprTestInfoServiceSDK.get(testId, null, null, null);
        markAnswerInfo.setTestId(testInfoBO.getTestId());
        markAnswerInfo.setExamId(testInfoBO.getExamId());
        markAnswerInfo.setTestName(testInfoBO.getTestName());
        markAnswerInfo.setTotalCount(CollectionUtils.isEmpty(testInfoBO.getRecipients()) ? 0 : testInfoBO.getRecipients().size());
        QueryAnswerDTO markAnswerDTO = new QueryAnswerDTO();
        markAnswerDTO.setTestId(testId);
        markAnswerDTO.setUserId(userId);
        markAnswerDTO.setGroupIds(groupIds);
        List<QueryTestUserResultDTO> testUserResultDTOS = pprTestUserInfoServiceSDK.queryAnswer(markAnswerDTO);
        markAnswerInfo.setSubmitCount(CollectionUtils.isEmpty(testUserResultDTOS) ?  0 : testUserResultDTOS.size());
        if (CollectionUtils.isNotEmpty(testUserResultDTOS)){
            markAnswerInfo.setAnswers(this.getStudentAnswers(BeanUtils.convert(testUserResultDTOS, TestUserInfoBO.class)));
        }
        return markAnswerInfo;
    }

    /**
     * Sort answers by students
     * @param testUsers
     * @return  key：questionId, value: Each student’s answer to this question
     */
    private List<UserAnswerInfoBO> getStudentAnswers(List<TestUserInfoBO> testUsers) {
        List<UserAnswerInfoBO> userAnswers = new ArrayList<UserAnswerInfoBO>();
        for (TestUserInfoBO testUser : testUsers) {
            for (UserAnswerInfoBO answerAnswer : testUser.getAnswers()) {
                answerAnswer.setTestUserId(testUser.getTestUserId());
                answerAnswer.setUserId(testUser.getUserId());
                answerAnswer.setUserName(testUser.getUserName());
                userAnswers.add(answerAnswer);
            }
        }
        return userAnswers;
    }

    /**
     * @param markInfo
     */
    public TestMarkResultInfoBO mark(MarkActionInfoBO markInfo) {
        MarkInfoDTO markDTO = new MarkInfoDTO();
        markDTO.setTestId(markInfo.getTestId());
        markDTO.setType(markInfo.getType());
        markDTO.setAnswers(BeanUtils.convert(markInfo.getAnswers(), MarkInfoDTO.MarkUserAnswerInfoDTO.class));
        TestMarkResultInfoDTO markResult = pprTestUserInfoServiceSDK.mark(markDTO);
        return BeanUtils.convert(markResult, TestMarkResultInfoBO.class);
    }

    /**
     * @param canvasInfoBO
     */
    public void editCanvas(UserAnswerCanvasInfoBO canvasInfoBO){
        pprTestUserInfoServiceSDK.editCanvas(BeanUtils.convert(canvasInfoBO, EditCanvasInfoDTO.class));
    }

}
