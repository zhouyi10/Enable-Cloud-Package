package com.enableets.edu.pakage.framework.ppr.test.service.submit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.framework.ppr.test.dao.TestInfoDAO;
import com.enableets.edu.pakage.framework.ppr.test.po.TestInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.service.BusinessOrderService;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.SubmitConstants;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.SubmitUtils;
import com.enableets.edu.pakage.framework.ppr.bo.BusinessOrderBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestRecipientInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.RecipientCacheMap;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Modification of the transfer method, without saving the file, directly writing to the database
 */
@Service
@Slf4j
public class SubmitAnswerService {

    /** 打卡任务节点分数通知 queue name */
    @Value("${paper.config.business.replay.step.queue:paper.step.score.push}")
    public String QUEUE_NAME_STEP_SCORE;

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private TestInfoDAO testInfoDAO;

    /**
     * 暂存考试结果
     * @param answerXml
     * @return
     */
    public String save(String answerXml) {
        Assert.hasText(answerXml, "answerXml cannot be empty!");
        BusinessOrderBO result = SaveAnswerXmlRunner.save(answerXml);
        return result == null ? null : result.getOrderId();
    }

    public void submitFromStep(SubmitAttributeBO attribute) {
        List<TestInfoPO> tests = testInfoDAO.getByActivityId(attribute.getStepId());
        if (CollectionUtils.isNotEmpty(tests) && (StringUtils.isNotBlank(attribute.getFileId()) || StringUtils.isNotBlank(attribute.getPaperId()) )) {
            Iterator<TestInfoPO> iter = tests.iterator();
            while (iter.hasNext()) {
                boolean ret = false;
                if (StringUtils.isNotBlank(attribute.getFileId()) && attribute.getFileId().equals(iter.next().getFileId())){  //默认交卷
                    ret = true;
                }
                if (StringUtils.isNotBlank(attribute.getPaperId()) && attribute.getPaperId().equals(iter.next().getExamId())) {  //PPR交卷
                    ret = true;
                }
                if (!ret) iter.remove();
            }
        }
        //2. 交卷
        if (CollectionUtils.isEmpty(tests)) {
            log.error("test not exists, {}", JsonUtils.convert(attribute));
            return;
        }

        for (TestInfoPO testInfo : tests) {
            String businessId = SubmitUtils.buildAnswerBusinessId(testInfo.getActivityId(), attribute.getUserId());
            BusinessOrderService businessOrderService = SpringBeanUtils.getBean(BusinessOrderService.class);
            BusinessOrderBO business = businessOrderService.get(businessId, SubmitConstants.SUBMIT_BUSINESS_TYPE);
            if (business == null) {
                log.error("student[{}] did not answer the exam[{}]", attribute.getUserId(), testInfo.getTestId());
                return;
            }
            SubmitAttributeBO obj = attribute.clone();
            obj.setTestId(testInfo.getTestId());
            SubmitAnswerRunner.submit(business, obj);
        }
    }

    public EnableCardPackage getUserAnswer2(String enableCardPackageStr){
        EnableCardPackage enableCardPackage = JsonUtils.convert(enableCardPackageStr, EnableCardPackage.class);
        if (enableCardPackage.getBody().getAction() == null || CollectionUtils.isEmpty(enableCardPackage.getBody().getAction().getItems())){
            throw new MicroServiceException("38-01-003", "Submit Action not found!");
        }
        ActionItem action = enableCardPackage.getBody().getAction().getItems().stream().filter(e -> e.getType().equals(PPRConstants.STEP_ACTION_SUBMIT_PAPER_TYPE)).collect(Collectors.toList()).get(0);
        if (action == null) {
            throw new MicroServiceException("38-01-003", "Submit Action not found!");
        }
        if (enableCardPackage.getBody().getAnswer() == null || CollectionUtils.isEmpty(enableCardPackage.getBody().getAnswer().getAnswerItems())){
            log.error( "userAnswers cannot be empty!");
            throw new MicroServiceException("38-01-004", "User answers info not found");
        }
        TestInfoBO testInfoBO = testInfoService.get(null, action.getId(), null, enableCardPackage.readRefId(), Boolean.FALSE);
        if (testInfoBO == null){
            log.error("test Info not found");
            throw new MicroServiceException("38-01-005", "Test Info not found");
        }
        TestRecipientInfoBO recipientInfoBO = RecipientCacheMap.get(testInfoBO.getTestId(), action.readProperty("userId"));
        if (recipientInfoBO == null){
            log.error("Test no send to the student["+action.readProperty("userId")+", "+action.readProperty("fullName")+"]");
            throw new MicroServiceException("38-01-006", "Test no send to the student[" + action.readProperty("userId") + ", "+ action.readProperty("fullName") +"]");
        }
        return enableCardPackage;
    }

}
