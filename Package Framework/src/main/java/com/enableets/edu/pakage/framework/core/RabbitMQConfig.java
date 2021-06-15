package com.enableets.edu.pakage.framework.core;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/05/11
 **/

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_SEND_PAPER = "StudyTaskQueue";

    public static final String QUEUE_STEP_INSTANCE = "StepInstanceQueue";

    public static final String QUEUE_SUBMIT = "package_submit";

    public static final String EXCHANGE_SUBMIT_DIRECT = "package_submit_direct";

    public static final String ROUTING_KEY_SUBMIT_ANSWER = "answer";

    @Bean
    public Queue queueSendPaper(){
        return QueueBuilder.durable(QUEUE_SEND_PAPER).build();
    }

    @Bean
    public Queue queueStepInstance() {
        return QueueBuilder.durable(QUEUE_STEP_INSTANCE).build();
    }

    @Bean
    public Queue queueSubmit() {
        return QueueBuilder.durable(QUEUE_SUBMIT).build();
    }

    @Bean
    public Exchange exchangeSubmit() {
        return ExchangeBuilder.directExchange(EXCHANGE_SUBMIT_DIRECT).durable(true).build();
    }

    @Bean
    public Binding submitAnswerBing() {
        return BindingBuilder.bind(queueSubmit()).to(exchangeSubmit()).with(ROUTING_KEY_SUBMIT_ANSWER).noargs();
    }

    private static final String QUEUE_USER_ASSESSMENT_RESULT = "queue_user_assessment_result";

    public static final String EXCHANGE_USER_ASSESSMENT_RESULT = "exchange_user_assessment_result";

    public static final String ROUTING_KEY_USER_ASSESSMENT_RESULT = "routing_key_user_assessment_result";

    @Bean
    public Queue queueUserAssessmentResult(){
        return QueueBuilder.durable(QUEUE_USER_ASSESSMENT_RESULT).build();
    }

    @Bean
    public Exchange exchangeUserAssessmentResult(){
        return ExchangeBuilder.directExchange(EXCHANGE_USER_ASSESSMENT_RESULT).build();
    }

    @Bean
    public Binding bindingUserAssessmentResult(){
        return BindingBuilder.bind(queueUserAssessmentResult()).to(exchangeUserAssessmentResult()).with(ROUTING_KEY_USER_ASSESSMENT_RESULT).noargs();
    }
}
