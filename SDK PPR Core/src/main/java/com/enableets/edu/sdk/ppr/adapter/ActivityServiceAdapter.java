package com.enableets.edu.sdk.ppr.adapter;

import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.utils.Utils;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.core.JsonUtils;
import com.enableets.edu.sdk.ppr.http.HttpClientFactory;
import com.enableets.edu.sdk.ppr.http.IHttpClient;
import com.enableets.edu.sdk.ppr.ppr.bo.steptask.CheckInStepTaskBO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/16
 **/
public class ActivityServiceAdapter {

    public static StepInfoBO getStepInfo(String stepId, Configuration configuration){
        if (StringUtils.isBlank(stepId)) return null;
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String url = configuration.getServerAddress() + "/microservice/activityservice/v1/steptasks/steps/" + stepId;
        String paperStr = httpClient.doGet(url);
        String stepStr = Utils.responseDataHandler(paperStr);
        return JsonUtils.convert(stepStr, StepInfoBO.class);
    }

    public static void checkIn(CheckInStepTaskBO checkInStepTaskBO, Configuration configuration){
        String url = configuration.getServerAddress() + "/microservice/activityservice/v3/steptasks/"+checkInStepTaskBO.getActivityId()+"/users/" + checkInStepTaskBO.getUserId();
        IHttpClient httpClient = HttpClientFactory.getHttpClient(configuration);
        String rJson = httpClient.doPost(url, JsonUtils.convert(checkInStepTaskBO.getStepInstances()));
        Utils.responseDataHandler(rJson);
    }
}
