package com.enableets.edu.pakage.framework.ppr.paper.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.core.CacheableKeyGeneratorConfig;
import com.enableets.edu.pakage.framework.ppr.bo.xkw.*;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.XKWConfigReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 学科网服务
 *
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@Service
@Slf4j
public class XKWService {

    @Autowired
    private XKWConfigReader xkwConfigReader;

    private static String XWKAuthorization;

    /**
     * 获取所有知识点根信息
     *
     * @return java.util.List<com.enableets.edu.pakage.framework.ppr.bo.xkw.XKWBlankBO>
     * @date 2021/06/28 16:12
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWBlankBO> getCategoryRoots() {
        HttpRequest xkwHttpRequest = this.getXKWHttpRequest(xkwConfigReader.getGetAllCategoryRootUrl(), Method.GET);
        HttpResponse request = xkwHttpRequest.execute();
        if (!request.isOk()) {
            log.error("XKW getAllCategoryRoot error, request status:[{}], body: [{}]", request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW getAllCategoryRoot interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWBlankBO>>() {
        });
    }

    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "#result == null")
    public XKWCategoryBO queryCategoryByRootId(String categoryRootId) {
        HttpRequest xkwHttpRequest = this.getXKWHttpRequest(String.format(xkwConfigReader.getQueryCategoryByRootId(), categoryRootId), Method.GET);
        HttpResponse request = xkwHttpRequest.execute();
        if (!request.isOk()) {
            log.error("XKW queryCategoryByCategoryRootId error, request status:[{}], body: [{}]", request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW queryCategoryByCategoryRootId interface error");
        }
        return JSON.parseObject(request.body(), XKWCategoryBO.class);
    }

    /**
     * 搜索试题列表 - 根据指定的条件搜索相关试题列表
     *
     * @param params : 指定条件
     * @return XKWRequestListBO
     * @date 2021/06/10 11:39
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWQuestionBO> searchQuestions(XKWSearchQuestionParamBO params) {
        HttpRequest xkwHttpRequest = this.getXKWHttpRequest(String.format(xkwConfigReader.getSearchQuestionsUrl(), String.valueOf(params.getBlankId())), Method.GET);
        params.setBlankId(null);
        HttpResponse request = xkwHttpRequest
                .form(BeanUtil.beanToMap(params, false, true)).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestions error, params: [{}], request status:[{}], body: [{}]", JSONUtil.toJsonStr(params), request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW searchQuestions interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWQuestionBO>>() {
        });
    }

    /**
     * 拍图搜题 - 通过base64图像搜索试题
     *
     * @param content      : 搜索的文本（图片和文本同时传入，优先按照文本匹配）
     * @param base64ImgStr : 图片base64数据
     * @return XKWRequestListBO
     * @date 2021/06/10 17:23
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWQuestionBO> searchQuestionByPhoto(String content, String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("content", content);
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.getXKWHttpRequest(xkwConfigReader.getSearchQuestionsByPhotoUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByPhoto error, params content:[{}], base64ImgStr:\n[{}], \nrequest status:[{}], body: [{}]", content, base64ImgStr, request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW searchQuestionByPhoto interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWQuestionBO>>() {
        });
    }

    /**
     * 拍照搜题 - 通过base64图像搜索试题
     *
     * @param base64ImgStr : 图片base64数据
     * @return XKWRequestListBO
     * @date 2021/06/10 17:23
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWQuestionBO> searchQuestionByOCR(String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.getXKWHttpRequest(xkwConfigReader.getSearchQuestionsByOCRUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByOCR error, params base64ImgStr:\n[{}], \nrequest status:[{}], body: [{}]", base64ImgStr, request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW searchQuestionByOCR interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWQuestionBO>>() {
        });
    }

    /**
     * 拍照搜题(新版) - 通过base64图像搜索试题(0-只返回组卷试题 1-只返回好未来试题 2-返回好未来和组卷试题)
     *
     * @param base64ImgStr :
     * @return XKWRequestListBO
     * @date 2021/06/10 17:25
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWQuestionBO> searchQuestionByPicture(String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.getXKWHttpRequest(xkwConfigReader.getSearchQuestionsByPictureUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByPicture error, params base64ImgStr:\n[{}], \nrequest status:[{}], body: [{}]", base64ImgStr, request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW searchQuestionByPicture interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWQuestionBO>>() {
        });
    }

    /**
     * 推荐试题列表 - 根据指定的试题ID获取同学科下的推荐试题列表
     *
     * @param blankId    : 基础数据-题库id
     * @param questionId : 试题id
     * @param pageSize   : 推荐试题数量（1~10）
     * @return XKWRequestListBO
     * @date 2021/06/10 17:27
     * @since caleb_liu@enable-ets.com
     */
    @Cacheable(value = CacheableKeyGeneratorConfig.XKW_CACHE_KEY, keyGenerator = CacheableKeyGeneratorConfig.DEFAULT_KEY_GENERATOR,
            unless = "T(org.apache.commons.collections.CollectionUtils).isEmpty(#result)")
    public List<XKWQuestionBO> searchQuestionByRecommend(String blankId, String questionId, Integer pageSize) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("pageSize", pageSize);
        HttpResponse request = this.getXKWHttpRequest(String.format(xkwConfigReader.getSearchQuestionsByRecommendUrl(), blankId, questionId), Method.GET)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByRecommend error, params blankId:[{}], questionId:[{}], pageSize:[{}], \nrequest status:[{}], body: [{}]",
                    blankId, questionId, pageSize,
                    request.getStatus(), request.body());
            throw new MicroServiceException(PPRConstants.XKW_INTERFACE_ERROR_CODE, "XKW searchQuestionByRecommend interface error");
        }
        return JSON.parseObject(request.body(), new TypeReference<List<XKWQuestionBO>>() {
        });
    }


    /**
     * 构建学科网请求
     *
     * @return cn.hutool.http.HttpRequest
     * @date 2021/06/10 16:16
     * @since caleb_liu@enable-ets.com
     */
    public HttpRequest getXKWHttpRequest(String interfaceUrl, Method method) {
        return HttpUtil.createRequest(method, xkwConfigReader.getDomain() + interfaceUrl).header("Authorization", this.getXWKAuthorization()).timeout(xkwConfigReader.getTimeout());
    }


    /**
     * 构建学科网认证头
     *
     * @return java.lang.String
     * @date 2021/06/10 15:58
     * @since caleb_liu@enable-ets.com
     */
    @SneakyThrows
    public String getXWKAuthorization() {
        if (StrUtil.isNotBlank(this.XWKAuthorization)) return this.XWKAuthorization;
        String key = new StringBuilder(xkwConfigReader.getAppId()).append(":").append(xkwConfigReader.getSecret()).toString();
        this.XWKAuthorization = "Basic " + Base64.getEncoder().encodeToString(key.getBytes("UTF-8"));
        return this.XWKAuthorization;
    }

}
