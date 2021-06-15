package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.pakage.ppr.dto.AddAnswerCardInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRAnswerCardInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRAnswerCardInfoService;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
public class DefaultPPRAnswerCardInfoService implements IPPRAnswerCardInfoService {

    private IPPRAnswerCardInfoServiceFeignClient pprAnswerCardInfoServiceFeignClient;

    public DefaultPPRAnswerCardInfoService(IPPRAnswerCardInfoServiceFeignClient pprAnswerCardInfoServiceFeignClient){
        this.pprAnswerCardInfoServiceFeignClient = pprAnswerCardInfoServiceFeignClient;
    }

    @Override
    public QueryAnswerCardInfoResultDTO getAnswerCard(String paperId, String creator) {
        return pprAnswerCardInfoServiceFeignClient.getAnswerCard(paperId, creator).getData();
    }

    @Override
    public QueryAnswerCardInfoResultDTO add(AddAnswerCardInfoDTO answerCardInfo) {
        return pprAnswerCardInfoServiceFeignClient.add(answerCardInfo).getData();
    }

    @Override
    public List<QueryAnswerCardInfoResultDTO> query(Long examId, String creator) {
        return pprAnswerCardInfoServiceFeignClient.query(examId, creator).getData();
    }
}
