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
            // ?????????????????????
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_SUBMIT_DIRECT, RabbitMQConfig.ROUTING_KEY_SUBMIT_ANSWER, msg.getBytes());
            channel.basicAck(deliveryTag, false);
        } else {
            // ?????????, ????????????????????????
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
                // ????????????????????????, ????????????, ????????????
                log.error("processSubmitAnswerMessage ????????????????????????, msg: " + msg);
                channel.basicReject(deliveryTag, false);
                return;
            }
            try {
                if (StringUtils.isBlank(stepSubmitInfoMsgBO.getBusinessId())) channel.basicReject(deliveryTag, false);
                // ?????????, ?????????????????????5??????, ?????????????????????
                submit(stepSubmitInfoMsgBO.getBusinessId(), stepSubmitInfoMsgBO.getStepInstanceId());
                channel.basicAck(deliveryTag, false);
            } catch (Exception e) {
                channel.basicReject(deliveryTag, true);
            }
        } else {
            // ?????????, ????????????????????????
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
            // ???????????? ?????? actionflow????????????
            answerRequestData.setStatus(1);
            answerRequestDataService.update(answerRequestData);
        } catch (Throwable e) {
            if (e instanceof MicroServiceException) {
                MicroServiceException eTemp = (MicroServiceException) e;
                answerRequestData.setErrorCode(eTemp.getErrorCode());
                answerRequestData.setErrorMessage(eTemp.getMessage() + System.lineSeparator() + "---??????????????????:" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            } else {
                answerRequestData.setErrorCode("6000");
                answerRequestData.setErrorMessage("????????????:" + e.getMessage() + System.lineSeparator() + "---??????????????????:" + System.lineSeparator() + ExceptionUtils.getStackTrace(e));
            }
            if (answerRequestData.getRetryTimes() < 5) {
                answerRequestData.setStatus(-1);
                answerRequestData.setRetryTimes(answerRequestData.getRetryTimes() + 1);
                answerRequestDataService.update(answerRequestData);
                // ?????????????????????, ???????????????.
                throw e;
            }
        }
    }

}
