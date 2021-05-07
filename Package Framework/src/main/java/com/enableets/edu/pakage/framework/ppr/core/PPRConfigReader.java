package com.enableets.edu.pakage.framework.ppr.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
@Component
@Data
public class PPRConfigReader {

    @Value("${package.config.ppr.dir.temp}")
    private String pprMakeTempDir;

    @Value("${package.config.ppr.offline-paper.origin-path}")
    private String offlinePaperOriginPath;

    @Value("${package.config.ppr.offline-paper.temp-path}")
    private String offlinePaperTempPath;

    @Value("${picture.url}")
    private String pictureUrl;

    @Value("${package.config.ppr.step-answer.type:step_type}")
    private String[] activityTypeStepArr;
}
