package com.enableets.edu.pakage.framework.ppr.test.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/02/03
 **/

@ConfigurationProperties(prefix="answer-data-processor")
@Component
@Data
public class AnswerDataProcessorConfig {
    Integer corePoolSize = 2;
    Integer poolSize = 5;
    Integer poolMaxSize = 10;
    Integer keepAliveTime = 1000;
}
