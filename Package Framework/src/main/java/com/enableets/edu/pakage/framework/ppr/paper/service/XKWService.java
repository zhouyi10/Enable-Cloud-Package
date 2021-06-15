package com.enableets.edu.pakage.framework.ppr.paper.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.enableets.edu.pakage.framework.ppr.bo.xkw.XKWQuestionBO;
import com.enableets.edu.pakage.framework.ppr.bo.xkw.XKWRequestListBO;
import com.enableets.edu.pakage.framework.ppr.bo.xkw.XKWSearchQuestionParamBO;
import com.enableets.edu.pakage.framework.ppr.core.XKWConfigReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;

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
     * 搜索试题列表 - 根据指定的条件搜索相关试题列表
     *
     * @date 2021/06/10 11:39
     * @since caleb_liu@enable-ets.com
     * @param params : 指定条件
     * @return XKWRequestListBO
     */
    public XKWRequestListBO searchQuestions(XKWSearchQuestionParamBO params) {
        HttpResponse request = this.buildXKWHttpRequest(String.format(xkwConfigReader.getSearchQuestionsUrl(), params.getBlankId()), Method.GET)
                .form(BeanUtil.beanToMap(params)).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestions error, params: [{}], \nrequest detail: [{}]", JSONUtil.toJsonStr(params), JSONUtil.toJsonStr(request));
        }
        return JSONUtil.toBean(request.body(), new TypeReference<XKWRequestListBO<XKWQuestionBO>>(){}, true);
    }

    /**
     * 拍图搜题 - 通过base64图像搜索试题
     * 
     * @date 2021/06/10 17:23
     * @since caleb_liu@enable-ets.com
     * @param content : 搜索的文本（图片和文本同时传入，优先按照文本匹配）
	 * @param base64ImgStr : 图片base64数据
     * @return XKWRequestListBO
     */
    public XKWRequestListBO searchQuestionByPhoto(String content, String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("content", content);
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.buildXKWHttpRequest(xkwConfigReader.getSearchQuestionsByPhotoUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByPhoto error, params content:[{}], base64ImgStr:\n[{}], \nrequest detail: [{}]", content, base64ImgStr, JSONUtil.toJsonStr(request));
        }
        return JSONUtil.toBean(request.body(), new TypeReference<XKWRequestListBO<XKWQuestionBO>>(){}, true);
    }

    /**
     * 拍照搜题 - 通过base64图像搜索试题
     * 
     * @date 2021/06/10 17:23
     * @since caleb_liu@enable-ets.com
     * @param base64ImgStr : 图片base64数据
     * @return XKWRequestListBO
     */
    public XKWRequestListBO searchQuestionByOCR(String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.buildXKWHttpRequest(xkwConfigReader.getSearchQuestionsByOCRUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByOCR error, params base64ImgStr:\n[{}], \nrequest detail: [{}]", base64ImgStr, JSONUtil.toJsonStr(request));
        }
        return JSONUtil.toBean(request.body(), new TypeReference<XKWRequestListBO<XKWQuestionBO>>(){}, true);
    }

    /**
     * 拍照搜题(新版) - 通过base64图像搜索试题(0-只返回组卷试题 1-只返回好未来试题 2-返回好未来和组卷试题)
     *
     * @date 2021/06/10 17:25
     * @since caleb_liu@enable-ets.com
     * @param base64ImgStr :
     * @return XKWRequestListBO
     */
    public XKWRequestListBO searchQuestionByPicture(String base64ImgStr) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("imgStr", base64ImgStr);
        HttpResponse request = this.buildXKWHttpRequest(xkwConfigReader.getSearchQuestionsByPictureUrl(), Method.POST)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByPicture error, params base64ImgStr:\n[{}], \nrequest detail: [{}]", base64ImgStr, JSONUtil.toJsonStr(request));
        }
        return JSONUtil.toBean(request.body(), new TypeReference<XKWRequestListBO<XKWQuestionBO>>(){}, true);
    }

    /**
     * 推荐试题列表 - 根据指定的试题ID获取同学科下的推荐试题列表
     * 
     * @date 2021/06/10 17:27
     * @since caleb_liu@enable-ets.com
     * @param blankId : 基础数据-题库id
	 * @param questionId : 试题id
	 * @param pageSize : 推荐试题数量（1~10）
     * @return XKWRequestListBO
     */
    public XKWRequestListBO searchQuestionByRecommend(String blankId, String questionId, Integer pageSize) {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("pageSize", pageSize);
        HttpResponse request = this.buildXKWHttpRequest(String.format(xkwConfigReader.getSearchQuestionsByRecommendUrl(), blankId, questionId), Method.GET)
                .form(objectObjectHashMap).execute();
        if (!request.isOk()) {
            log.error("XKW searchQuestionByRecommend error, params blankId:[{}], questionId:[{}], pageSize:[{}], \nrequest detail: {}",
                    blankId, questionId, pageSize,
                    JSONUtil.toJsonStr(request));
        }
        return JSONUtil.toBean(request.body(), new TypeReference<XKWRequestListBO<XKWQuestionBO>>(){}, true);
    }


    /**
     * 构建学科网请求
     *
     * @date 2021/06/10 16:16
     * @since caleb_liu@enable-ets.com
     * @return cn.hutool.http.HttpRequest
     */
    public HttpRequest buildXKWHttpRequest(String url, Method method) {
        return HttpUtil.createRequest(method, url).header("Authorization", this.getXWKAuthorization());
    }

    
    /**
     * 构建学科网认证头
     *
     * @date 2021/06/10 15:58
     * @since caleb_liu@enable-ets.com
     * @return java.lang.String
     */
    @SneakyThrows
    public String getXWKAuthorization() {
        if (StrUtil.isNotBlank(this.XWKAuthorization)) return this.XWKAuthorization;
        String key = new StringBuilder(xkwConfigReader.getAppId()).append(":").append(xkwConfigReader.getSecret()).toString();
        this.XWKAuthorization = "Basic " + Base64.getEncoder().encodeToString(key.getBytes("UTF-8"));
        return this.XWKAuthorization;
    }

}
