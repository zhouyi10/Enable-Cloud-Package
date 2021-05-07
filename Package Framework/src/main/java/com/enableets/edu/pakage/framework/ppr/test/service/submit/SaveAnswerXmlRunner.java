package com.enableets.edu.pakage.framework.ppr.test.service.submit;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.framework.ppr.test.service.BusinessOrderService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.ConcurrencyUtils;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.SubmitConstants;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.SubmitUtils;
import com.enableets.edu.pakage.framework.ppr.bo.BusinessOrderBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duffy_ding
 * @since 2020/03/16
 */
@Component
public class SaveAnswerXmlRunner implements ApplicationRunner {

    /** save exam information thread pool */
    private static ConcurrencyUtils.UnionRequestExecutor saveDataExecutor;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        DynamicIntProperty dbProperty = DynamicPropertyFactory.getInstance().getIntProperty(SubmitConstants.En_SUBMIT_POOL_SIZE, 20);
        int poolSize = dbProperty.get();
        saveDataExecutor = ConcurrencyUtils.newUnionRequestBlockThreadPoolExecutor(this::mergeSave, poolSize);
    }

    /**
     * merge request save
     * @param paperCardXmlMap req - paperCardXmlMap map
     * @return req - business map
     */
    public Map<String, BusinessOrderBO> mergeSave(Map<String, String> paperCardXmlMap) {
        BusinessOrderService businessOrderService = SpringBeanUtils.getBean(BusinessOrderService.class);
        SubmitAnswerService submitAnswerService = SpringBeanUtils.getBean(SubmitAnswerService.class);
        Map<String, String> bizThreadMap = new HashMap<String, String>();
        List<BusinessOrderBO> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : paperCardXmlMap.entrySet()) {
            // 1. 校验交卷信息是否OK
            EnableCardPackage enableCardPackage = submitAnswerService.getUserAnswer2(entry.getValue());
            ActionItem actionBO = null;
            for (ActionItem action : enableCardPackage.getBody().getAction().getItems()) {
                if (PPRConstants.STEP_ACTION_SUBMIT_PAPER_TYPE.equals(action.getType())){
                    actionBO = action; break;
                }
            }
            BusinessOrderBO bo = businessOrderService.buildBusinessInfo(SubmitUtils.buildAnswerBusinessId(actionBO.getId(), actionBO.readProperty("userId")), SubmitConstants.SUBMIT_BUSINESS_TYPE, entry.getValue());
            list.add(bo);
            bizThreadMap.put(bo.getOrderId(), entry.getKey());
        }
        // 2. 保存待处理业务数据
        List<BusinessOrderBO> bizResults = businessOrderService.batchAdd(list, SubmitConstants.SUBMIT_BUSINESS_TYPE);
        Map<String, BusinessOrderBO> result = new HashMap<>();
        for (BusinessOrderBO bo : bizResults) {
            result.put(bizThreadMap.get(bo.getOrderId()), bo);
        }
        return result;
    }

    /**
     * 保存答题数据
     * @param answerXml 答案xml
     * @return 保存后业务数据
     */
    public static BusinessOrderBO save(String answerXml) {
        BusinessOrderBO result = (BusinessOrderBO) saveDataExecutor.mergeRequest(answerXml);
        if (result == null) {
            throw new MicroServiceException("38-01-002", "Fail to save test information！");
        }
        return result;
    }

    /**
     * get save answer executor status
     * @return executor status
     */
    public static String getStatus() {
        return saveDataExecutor.getStatus();
    }
}
