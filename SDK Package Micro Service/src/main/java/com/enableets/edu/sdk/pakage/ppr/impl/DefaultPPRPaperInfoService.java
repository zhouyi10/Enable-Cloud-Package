package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.pakage.ppr.dto.AddPaperInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPaperInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRPaperInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRPaperInfoService;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/09
 **/
public class DefaultPPRPaperInfoService implements IPPRPaperInfoService {

    private IPPRPaperInfoServiceFeignClient pprPaperInfoServiceFeignClient;

    public DefaultPPRPaperInfoService(IPPRPaperInfoServiceFeignClient pprPaperInfoServiceFeignClient){
        this.pprPaperInfoServiceFeignClient = pprPaperInfoServiceFeignClient;
    }

    @Override
    public QueryPaperInfoResultDTO get(Long paperId) {
        return pprPaperInfoServiceFeignClient.get(paperId).getData();
    }

    @Override
    public QueryPaperInfoResultDTO add(AddPaperInfoDTO addPaperInfoDTO) {
        return pprPaperInfoServiceFeignClient.add(addPaperInfoDTO).getData();
    }
}
