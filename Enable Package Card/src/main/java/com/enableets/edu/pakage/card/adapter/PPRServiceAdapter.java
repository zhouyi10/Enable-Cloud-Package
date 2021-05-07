package com.enableets.edu.pakage.card.adapter;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.core.http.HttpClientFactory;
import com.enableets.edu.pakage.core.core.http.IHttpClient;
import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;
import com.enableets.edu.pakage.core.utils.JsonUtils;

import cn.hutool.json.JSONObject;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/23
 **/
public class PPRServiceAdapter {

    private static final Log log = LogFactory.getLog(PPRServiceAdapter.class);

    public static boolean save(String enableCardXml, Configuration configuration){
        if (StringUtils.isBlank(enableCardXml)) return Boolean.FALSE;
        //String url = "http://192.168.118.12:9567" + "/microservice/packageservice/ppr/answer/submit";
        String url = configuration.getServerAddress() + "/microservice/packageservice/ppr/answer/submit";
        SubmitBO submitBO = new SubmitBO();
        submitBO.setEnableCardXml(enableCardXml);
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String rJson = httpClient.doPost(url, JsonUtils.convert(submitBO));
        String s = responseDataHandler(rJson);
        log.info("submit orderId = " + s);
        if (StringUtils.isNotBlank(s)) return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public static String responseDataHandler(String responseData){
        JSONObject json = new JSONObject(responseData);
        if (StringUtils.isNotBlank(responseData) && json.get("status").equals("success")){
            return json.getStr("data");
        }else{
            throw new PPRInterfaceAdapterException("Access interface is abnormal：status:" + json.get("statusCode"));
        }
    }
}
