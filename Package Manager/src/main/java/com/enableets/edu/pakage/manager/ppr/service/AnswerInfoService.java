package com.enableets.edu.pakage.manager.ppr.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerItem;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerQuestion;
import com.enableets.edu.pakage.card.bean.body.answer.Timestamp;
import com.enableets.edu.pakage.card.bean.body.answer.Trail;
import com.enableets.edu.pakage.card.bo.action.ActionMapper;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.ppr.bo.MarkActionInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.TestUserInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.UserAnswerCanvasInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.UserAnswerInfoBO;
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
import com.enableets.edu.sdk.pakage.ppr.dto.EditCanvasInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.MarkInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPPRInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestUserResultDTO;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRInfoService;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRTestInfoService;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRTestUserInfoService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    public void submit2(AnswerCardBO answerCardBO){
        //1、Get Recipient Info
        TestRecipientInfoBO recipientInfoBO = RecipientCacheMap.get(answerCardBO.getTestId(), answerCardBO.getUserId());
        //-----------------------------------------------
        //2、Get Package ppr
        EnableCardPackage enableCardPackage = this.getEnableCardPackage(answerCardBO.getPaperId());
        //3、Get EnableCard
        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        EnableCard enableCard = new EnableCard(enableCardPackage, configuration);
        //3.1 EnableCard Clock-In Step Action
        enableCard.getEnableCardPackage().getBody().setAction(this.getActions(answerCardBO.getStepId(), answerCardBO.getUserId(), recipientInfoBO.getUserName(), answerCardBO.getStartTime(), answerCardBO.getEndTime()));
        enableCard.clockIn();
        //3.2 EnableCard save (Submit)
        enableCard.getEnableCardPackage().getBody().setAnswer(this.convertAnswer(answerCardBO));
        enableCard.save();
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
        String cacheKey = new StringBuilder(PPRConstants.PPR_ENABLE_CARD_KEY_PREFIX).append(paperId).toString();
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
    public void mark(MarkActionInfoBO markInfo) {
        MarkInfoDTO markDTO = new MarkInfoDTO();
        markDTO.setTestId(markInfo.getTestId());
        markDTO.setType(markInfo.getType());
        markDTO.setAnswers(BeanUtils.convert(markInfo.getAnswers(), MarkInfoDTO.MarkUserAnswerInfoDTO.class));
        pprTestUserInfoServiceSDK.mark(markDTO);
    }

    /**
     * @param canvasInfoBO
     */
    public void editCanvas(UserAnswerCanvasInfoBO canvasInfoBO){
        pprTestUserInfoServiceSDK.editCanvas(BeanUtils.convert(canvasInfoBO, EditCanvasInfoDTO.class));
    }

}
