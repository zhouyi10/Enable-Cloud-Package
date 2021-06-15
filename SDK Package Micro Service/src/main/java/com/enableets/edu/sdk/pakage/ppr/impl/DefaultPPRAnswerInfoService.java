package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.core.MicroServiceException;
import com.enableets.edu.sdk.pakage.ppr.dto.AnswerCardSubmitInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRAnswerInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRAnswerInfoService;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/19
 **/
public class DefaultPPRAnswerInfoService implements IPPRAnswerInfoService {

    private IPPRAnswerInfoServiceFeignClient pprAnswerInfoServiceFeignClient;

    public DefaultPPRAnswerInfoService(IPPRAnswerInfoServiceFeignClient pprAnswerInfoServiceFeignClient){
        this.pprAnswerInfoServiceFeignClient = pprAnswerInfoServiceFeignClient;
    }


    @Override
    public String submit(AnswerCardSubmitInfoDTO answerCardSubmitInfoDTO){
        return pprAnswerInfoServiceFeignClient.submit(answerCardSubmitInfoDTO).getData();
    }
}
