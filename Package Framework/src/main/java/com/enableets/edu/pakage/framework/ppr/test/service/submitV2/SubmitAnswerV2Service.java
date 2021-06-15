package com.enableets.edu.pakage.framework.ppr.test.service.submitV2;

import cn.hutool.json.JSONUtil;
import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.framework.core.util.ExceptionUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.core.RabbitMQConfig;
import com.enableets.edu.pakage.framework.ppr.test.core.TestConstants;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerRequestDataService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.bo.SubmitAttributeBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.handler.SubmitAnswerXmlDirector;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo.AnswerRequestDataBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo.StepSubmitInfoMsgBO;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.processor.AnswerRequestCompressorRunner;
import com.enableets.edu.pakage.framework.ppr.utils.ZipUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/05/11
 **/

@Service
@Slf4j
public class SubmitAnswerV2Service {

    @Autowired
    private AnswerRequestCompressorRunner answerRequestCompressorRunner;

    @Autowired
    private AnswerRequestDataService answerRequestDataService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_STEP_INSTANCE)
    public void processStepSaveMessage(String msg, Channel channel, Message message) throws IOException {
        channel.basicQos(1);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if (StringUtils.isNotBlank(msg)) {
            // 转发至交卷队列
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_SUBMIT_DIRECT, RabbitMQConfig.ROUTING_KEY_SUBMIT_ANSWER, msg.getBytes());
            channel.basicAck(deliveryTag, false);
        } else {
            // 无数据, 垃圾消息直接丢弃
            channel.basicReject(deliveryTag, false);
        }

    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_SUBMIT)
    public void processSubmitAnswerMessage(String msg, Channel channel, Message message) throws IOException {
        channel.basicQos(1);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        if (StringUtils.isNotBlank(msg)) {
            StepSubmitInfoMsgBO stepSubmitInfoMsgBO = null;
            try {
                stepSubmitInfoMsgBO = JSONUtil.toBean(msg, StepSubmitInfoMsgBO.class);
            } catch (Exception e) {
                // 业务数据转换错误, 垃圾数据, 直接丢弃
                log.error("processSubmitAnswerMessage 业务数据转换失败, msg: " + msg);
                channel.basicReject(deliveryTag, false);
                return;
            }
            try {
                if (StringUtils.isBlank(stepSubmitInfoMsgBO.getBusinessId())) channel.basicReject(deliveryTag, false);
                // 交卷时, 当重试次数超过5次后, 将不会抛出异常
                submit(stepSubmitInfoMsgBO.getBusinessId(), stepSubmitInfoMsgBO.getStepInstanceId());
                channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
                channel.basicReject(deliveryTag, true);
            }
        } else {
            // 无数据, 垃圾消息直接丢弃
            channel.basicReject(deliveryTag, false);
        }
    }

    public OperationResult save(String enableCardXml) {
        Assert.hasText(enableCardXml, "enableCardXml cannot be empty!");
        AnswerRequestDataBO result = null;
        OperationResult operationResult = new OperationResult();
        result = answerRequestCompressorRunner.submitAnswer(enableCardXml);
        if (result == null) {
            throw new MicroServiceException(TestConstants.ERROR_CODE_SAVE_USER_ANSWER_FAILED, TestConstants.ERROR_MESSAGE_SAVE_USER_ANSWER_FAILED);
        }
        return operationResult.success(result.getAnswerRequestId());
    }

    public void submit(String answerRequestId, String testUserId) {
        if (StringUtils.isBlank(answerRequestId)) {
            throw new MicroServiceException(TestConstants.ERROR_CODE_GET_SUBMIT_ANSWER_DATA_FAILED, TestConstants.ERROR_MESSAGE_GET_SUBMIT_ANSWER_DATA_FAILED);
        }
        AnswerRequestDataBO answerRequestData = answerRequestDataService.getById(answerRequestId);
        if (answerRequestData == null || StringUtils.isBlank(answerRequestData.getOriginData())) {
            throw new MicroServiceException(TestConstants.ERROR_CODE_GET_SUBMIT_ANSWER_DATA_FAILED, TestConstants.ERROR_MESSAGE_GET_SUBMIT_ANSWER_DATA_FAILED);
        }
        try {
            answerRequestData.setStatus(2);
            answerRequestDataService.update(answerRequestData);
            String answerXml = ZipUtils.gunzip(answerRequestData.getOriginData());
            SubmitAttributeBO submitAttributeBO = new SubmitAttributeBO();
            submitAttributeBO.setTestUserId(testUserId);
            String submit = SubmitAnswerXmlDirector.getHandler(answerXml).submit(answerXml, submitAttributeBO);
            // 交卷完毕 调用 actionflow相关接口
            answerRequestData.setStatus(1);
            answerRequestDataService.update(answerRequestData);
        } catch (Throwable e) {
            if (e instanceof MicroServiceException) {
                MicroServiceException eTemp = (MicroServiceException) e;
                answerRequestData.setErrorCode(eTemp.getErrorCode());
                answerRequestData.setErrorMessage(eTemp.getMessage() + System.lineSeparator() + "---异常详细信息:" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            } else {
                answerRequestData.setErrorCode("6000");
                answerRequestData.setErrorMessage("未知异常:" + e.getMessage() + System.lineSeparator() + "---异常详细信息:" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            }
            if (answerRequestData.getRetryTimes() < 5) {
                answerRequestData.setStatus(-1);
                answerRequestData.setRetryTimes(answerRequestData.getRetryTimes() + 1);
                answerRequestDataService.update(answerRequestData);
                // 重试次数以内的, 让队列重试.
                throw e;
            }
        }
    }

}
