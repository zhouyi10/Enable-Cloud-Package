package com.enableets.edu.pakage.framework.ppr.test.service.submit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.ppr.test.service.BusinessOrderService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.handler.SubmitAnswerXmlDirector;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.ConcurrencyUtils;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.utils.SubmitConstants;
import com.enableets.edu.pakage.framework.ppr.bo.BusinessOrderBO;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author duffy_ding
 * @since 2020/01/08
 */
@Component
@Slf4j
public class SubmitAnswerRunner implements ApplicationRunner {

    public static final String TYPE_STEP_ANSWER = "_STEP";

    /** 保存考试信息线程池 */
    private static ThreadPoolExecutor submitExecutor;

    private static LinkedBlockingQueue<SubmitRunnable> orderQueue;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("start submit answer runner!");
        DynamicIntProperty dbProperty = DynamicPropertyFactory.getInstance().getIntProperty(SubmitConstants.En_SUBMIT_POOL_SIZE, 20);
        int poolSize = dbProperty.get();

        orderQueue = new LinkedBlockingQueue<>();
        submitExecutor = ConcurrencyUtils.newBlockThreadPool(poolSize);

        retryInterruptAnswer();
    }

    /**
     * step 4 补偿机制
     * 查询需要重试的答案
     */
    @Scheduled(cron = "${package.config.ppr.business.submit.retrySchedule}")
    public void retryFailedAnswer(){
        List<BusinessOrderBO> orders = businessOrderService().queryRetryOrder(SubmitConstants.SUBMIT_BUSINESS_TYPE);
        retry(orders);
    }

    /**
     * 监听mq队列，处理 step 提交答案
     * @param messageStr 消息字符串
     */
    @JmsListener(destination = SubmitConstants.QUEUE_STEP_SUBMIT_ANSWER)
    @Transactional
    public void submitStepAnswer(String messageStr) {
        messageStr = StringUtils.defaultString(messageStr);
        log.debug("Received step submission message, data:" + messageStr);
        try {
            JSONObject object = new JSONObject(messageStr);
            String activityInfo = object.getString("activityInfo");
            SubmitAttributeBO attribute = JsonUtils.convert(activityInfo, SubmitAttributeBO.class);
            attribute.setType(TYPE_STEP_ANSWER);
            if (StringUtils.isBlank(attribute.getStepId()) || StringUtils.isBlank(attribute.getParentActivityId()) || StringUtils.isBlank(attribute.getUserId())) {
                log.error("message of queue{} is incomplete, {}", SubmitConstants.QUEUE_STEP_SUBMIT_ANSWER, messageStr);
                return;
            }
            SpringBeanUtils.getBean(SubmitAnswerService.class).submitFromStep(attribute);
        } catch (Exception e) {
            log.error("process message[" + messageStr + "] info failed, " + e.getMessage(), e);
        }
    }

    /**
     * 重试中断的答案
     */
    public void retryInterruptAnswer(){
        List<BusinessOrderBO> orders = businessOrderService().queryInterruptOrder(SubmitConstants.SUBMIT_BUSINESS_TYPE);
        retry(orders);
    }

    /**
     * 重试指定的order
     * @param orderIds orderIds
     */
    public static void retryOrder(List<String> orderIds) {
        List<BusinessOrderBO> bos = businessOrderService().querySubmitAnswerBusiness(orderIds, null, null, null, null, null, null, null);
        retry(bos);
    }

    /**
     * 重试
     * @param orders orders
     */
    public static void retry(List<BusinessOrderBO> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        Set<String> orderIds = orderQueue.stream().map(SubmitRunnable::getOrderId).collect(Collectors.toSet());
        for (BusinessOrderBO order : orders) {
            if (orderQueue.contains(order.getOrderId()) || orderIds.contains(order.getOrderId())) {
                continue;
            }
            SubmitAttributeBO attribute = JsonUtils.convert(order.getExtendAttrs(), SubmitAttributeBO.class);
            if (TYPE_STEP_ANSWER.equals(attribute.getType())) {
                if (StringUtils.isBlank(attribute.getTestId()) || StringUtils.isBlank(attribute.getStepId())) {
                    log.error("order [{}]: business data is incomplete! testId-{}, stepId-{}", order.getOrderId(), attribute.getTestId(), attribute.getStepId());
                    continue;
                }
            } else {
                continue;
            }
            submit(order, attribute);
        }
    }

    public static void submit(BusinessOrderBO business, SubmitAttributeBO attribute) {
        Assert.notNull(business, "business cannot be empty!");
        Assert.notNull(attribute, "attribute cannot be empty!");

        // 开始处理 保存业务数据
        BusinessOrderService businessOrderService = SpringBeanUtils.getBean(BusinessOrderService.class);
        businessOrderService.start(business.getOrderId(), JsonUtils.convert(attribute));
        SubmitAnswerRunner.SubmitRunnable task = new SubmitAnswerRunner.SubmitRunnable(business.getOrderId(), business.getOriginData(), attribute);
        try {
            orderQueue.put(task);
        } catch (InterruptedException ex) {
            log.error("Error enqueue to process add record! orderId: " + business.getOrderId());
            return;
        }
        submitExecutor.submit(task);
    }

    /**
     * statistics info
     * @param testId test id
     * @return
     */
    public static Map<String, Object> statistics(String testId) {
        Map<String, Object> result = new HashMap<>();
        result.put("processList", orderQueue.stream().collect(Collectors.toList()));
        result.put("saveExecutorStatus", SaveAnswerXmlRunner.getStatus());
        result.put("submitExecutorStatus", getSubmitExecutorStatus());
        result.put("statistics", businessOrderService().statistics(testId));
        return result;
    }

    /**
     * @return business order service
     */
    private static BusinessOrderService businessOrderService() {
        return SpringBeanUtils.getBean(BusinessOrderService.class);
    }

    /**
     * 提交线程池状态
     * @return status str
     */
    public static String getSubmitExecutorStatus() {
        return String.format("%d-%d-%d-%d:%d-%d-%d-%d",
                submitExecutor.getMaximumPoolSize(), submitExecutor.getLargestPoolSize(), submitExecutor.getPoolSize(), submitExecutor.getCorePoolSize(),
                submitExecutor.getTaskCount(), submitExecutor.getCompletedTaskCount(), submitExecutor.getActiveCount(), orderQueue.size());
    }

    public static class SubmitRunnable implements Runnable {

        private static final Logger LOGGER = LoggerFactory.getLogger(SubmitRunnable.class);

        private String orderId;

        private String userAnswers;

        private SubmitAttributeBO attribute;

        public SubmitRunnable(String orderId, String userAnswers, SubmitAttributeBO attribute) {
            this.orderId = orderId;
            this.userAnswers = userAnswers;
            this.attribute = attribute;
        }

        @Override
        public void run() {
            BusinessOrderService businessOrderService = SpringBeanUtils.getBean(BusinessOrderService.class);
            try {
                SpringBeanUtils.getBean(TransactionTemplate.class).execute(status -> {
                    // 1. submit data
                    //String testUserId = SpringBeanUtils.getBean(AnswerService.class).submit(userAnswers, attribute);
                    String testUserId = SubmitAnswerXmlDirector.getHandler(userAnswers).submit(userAnswers, attribute);
                    // 2. 保存成功修改状态
                    attribute.setTestUserId(testUserId);
                    businessOrderService.success(orderId, JsonUtils.convert(attribute));
                    return null;
                });
            } catch (Exception e) {
                String errorCode = e instanceof MicroServiceException ? ((MicroServiceException) e).getErrorCode() : null;
                businessOrderService.fail(orderId, errorCode, e.getMessage() + e.toString());
                LOGGER.error("[" + orderId + "] upload answer info failed, " + e.getMessage(), e);
            }
            orderQueue.remove(this); // 从队列里移除
        }

        /**
         * get task order id
         * @return order id
         */
        public String getOrderId() {
            return orderId;
        }
    }
}
