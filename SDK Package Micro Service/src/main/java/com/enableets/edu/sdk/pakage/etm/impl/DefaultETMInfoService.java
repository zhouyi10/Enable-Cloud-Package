package com.enableets.edu.sdk.pakage.etm.impl;

import com.enableets.edu.sdk.pakage.etm.dto.AddETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMPageDTO;
import com.enableets.edu.sdk.pakage.etm.dto.EditETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.QueryETMInfoResultDTO;
import com.enableets.edu.sdk.pakage.etm.feign.IETMInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.etm.service.IETMInfoService;

public class DefaultETMInfoService implements IETMInfoService {


    private IETMInfoServiceFeignClient ietmInfoServiceFeignClient;

    public DefaultETMInfoService(IETMInfoServiceFeignClient ietmInfoServiceFeignClient) {
        this.ietmInfoServiceFeignClient = ietmInfoServiceFeignClient;
    }

    @Override
    public QueryETMInfoResultDTO addETMInfo(AddETMInfoDTO addETMInfoDTO) {

        return ietmInfoServiceFeignClient.addETMInfo(addETMInfoDTO).getData();
    }

    @Override
    public QueryETMInfoResultDTO parseETMInfo(Long contentId) {
        return ietmInfoServiceFeignClient.parseETMInfo(contentId).getData();
    }

    @Override
    public QueryETMInfoResultDTO editETMInfo(EditETMInfoDTO editETMInfoDTO) {
        return ietmInfoServiceFeignClient.editETMInfo(editETMInfoDTO).getData();
    }

    @Override
    public QueryETMInfoResultDTO editOnePage(AddETMPageDTO addETMPageDTO) {
        return ietmInfoServiceFeignClient.editOnePage(addETMPageDTO).getData();
    }

    @Override
    public Boolean deleteOnePage(String pageInfoId) {
        Boolean result = ietmInfoServiceFeignClient.deleteOnePage(pageInfoId).getData();
        return result;
    }

    @Override
    public QueryETMInfoResultDTO queryETMInfo(String bookId) {
        return ietmInfoServiceFeignClient.queryETMInfo(bookId).getData();
    }
}
