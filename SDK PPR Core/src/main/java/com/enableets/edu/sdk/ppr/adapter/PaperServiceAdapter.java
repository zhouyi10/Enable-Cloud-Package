package com.enableets.edu.sdk.ppr.adapter;

import com.alibaba.fastjson.JSON;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.core.BeanUtils;
import com.enableets.edu.sdk.ppr.core.JsonUtils;
import com.enableets.edu.sdk.ppr.http.HttpClientFactory;
import com.enableets.edu.sdk.ppr.http.IHttpClient;
import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import com.enableets.edu.sdk.ppr.ppr.bo.card.AnswerCardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardAxisBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.utils.Utils;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/23
 **/
public class PaperServiceAdapter {

    private static final Log log = LogFactory.getLog(PaperServiceAdapter.class);

    public static PPRBO get(String paperId, Configuration configuration){
        if (StringUtils.isBlank(paperId)) return null;
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        //String url = configuration.getServerAddress() + "/microservice/paper/get?paperId=" + paperId;
        String url = "http://192.168.118.12:9567/microservice/packageservice/ppr/paper/"+paperId;
        String paperStr = httpClient.doGet(url);
        String dataStr = Utils.responseDataHandler(paperStr);
        return JSON.parseObject(dataStr, PPRBO.class);
    }

    public static AnswerCardBO getAnswerCardAxis(String paperId, String creator ,Configuration configuration){
        if (StringUtils.isBlank(paperId)) return null;
        String url = configuration.getServerAddress() + "/microservice/assessmentservice/v1/answercards?examId=" + paperId + "&creator=" + creator;
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String answerCardAxisStr = httpClient.doGet(url);
        return analysisAnswerCardAxis(answerCardAxisStr, paperId);
    }

    public static boolean save(String paperCardXMl, Configuration configuration){
        if (StringUtils.isBlank(paperCardXMl)) return Boolean.FALSE;
        //String url = "http://192.168.118.12:9567" + "/microservice/packageservice/ppr/answer/submit";
        String url = configuration.getServerAddress() + "/microservice/packageservice/ppr/answer/submit";
        SubmitBO submitBO = new SubmitBO();
        submitBO.setPaperCardXml(paperCardXMl);
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String rJson = httpClient.doPost(url, JsonUtils.convert(submitBO));
        String s = Utils.responseDataHandler(rJson);
        log.info("submit orderId = " + s);
        if (StringUtils.isNotBlank(s)) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    private static AnswerCardBO analysisAnswerCardAxis(String answerCardAxisStr, String paperId){
        try{
            String dataStr = Utils.responseDataHandler(answerCardAxisStr);
            List<AnswerCardBO> answerCardBOS = JsonUtils.convert2List(dataStr, AnswerCardBO.class);
            return answerCardBOS.get(0);
        }catch (Exception e){
            throw new PPRInterfaceAdapterException("Get answerCardAxis fail by id '"+paperId+"'");
        }
    }

}
