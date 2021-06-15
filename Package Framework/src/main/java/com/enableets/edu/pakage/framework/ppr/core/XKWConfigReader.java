package com.enableets.edu.pakage.framework.ppr.core;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@ConfigurationProperties(prefix = "xkw")
@Configuration
@Data
public class XKWConfigReader {

    @Value("${xkw.appId:xuechuang")
    private String appId;

    @Value("${xkw.secret:1cb69ce3943d4b58838f47fd37943cd2")
    private String secret;

    @Value("${xkw.interface.search-quesitons:/questions/%s")
    private String searchQuestionsUrl;

    @Value("${xkw.interface.search-quesitons-photo:/questions/photo/search")
    private String searchQuestionsByPhotoUrl;

    @Value("${xkw.interface.search-quesitons-ocr:/questions/ocr/search")
    private String searchQuestionsByOCRUrl;

    @Value("${xkw.interface.search-quesitons-picture:/questions/picture/search")
    private String searchQuestionsByPictureUrl;

    @Value("${xkw.interface.searcg-quesitons-recommend:/questions/{bankId}/recommend/{questionId}")
    private String searchQuestionsByRecommendUrl;
}
