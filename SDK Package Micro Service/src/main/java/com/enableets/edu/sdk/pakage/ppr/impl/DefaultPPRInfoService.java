package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.pakage.ppr.dto.AddPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.EditPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPPRInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRInfoService;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
public class DefaultPPRInfoService implements IPPRInfoService {

    private IPPRInfoServiceFeignClient pprInfoServiceFeignClient;

    public DefaultPPRInfoService(IPPRInfoServiceFeignClient pprInfoServiceFeignClient){
        this.pprInfoServiceFeignClient = pprInfoServiceFeignClient;
    }

    @Override
    public QueryPPRInfoResultDTO add(AddPPRInfoDTO addPPRInfoDTO) {
        return pprInfoServiceFeignClient.add(addPPRInfoDTO).getData();
    }

    @Override
    public QueryPPRInfoResultDTO get(String id) {
        return pprInfoServiceFeignClient.get(id).getData();
    }

    @Override
    public QueryPPRInfoResultDTO edit(EditPPRInfoDTO editPPRInfoDTO) {
        return pprInfoServiceFeignClient.edit(editPPRInfoDTO.getPaperId(), editPPRInfoDTO).getData();
    }
}
