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

    @Value("${xkw.appId:xuechuang_1}")
    private String appId;

    @Value("${xkw.secret:eee3fd99600144c8877107edd22b8de4}")
    private String secret;

    @Value("${xkw.interface.domain:http://api.xkw.com}")
    private String domain;

    @Value("${xkw.img.domain:http://api.xkw.com}")
    private String imgDomain;

    @Value("${xkw.interface.get-all-category-root:/zujuan/v2/banks/category_root}")
    private String getAllCategoryRootUrl;

    @Value("${xkw.interface.query-category-by-root-id:/zujuan/v2/categories/%s}")
    private String queryCategoryByRootId;

    @Value("${xkw.interface.search-quesitons:/zujuan/v2/questions/%s/search}")
    private String searchQuestionsUrl;

    @Value("${xkw.interface.search-quesitons-photo:/zujuan/v2/questions/photo/search}")
    private String searchQuestionsByPhotoUrl;

    @Value("${xkw.interface.search-quesitons-ocr:/zujuan/v2/questions/ocr/search}")
    private String searchQuestionsByOCRUrl;

    @Value("${xkw.interface.search-quesitons-picture:/zujuan/v2/questions/picture/search}")
    private String searchQuestionsByPictureUrl;

    @Value("${xkw.interface.searcg-quesitons-recommend:/zujuan/v2/questions/%s/recommend/%s}")
    private String searchQuestionsByRecommendUrl;

    @Value("${xkw.interface.timeout:1000}")
    private int timeout;
}
