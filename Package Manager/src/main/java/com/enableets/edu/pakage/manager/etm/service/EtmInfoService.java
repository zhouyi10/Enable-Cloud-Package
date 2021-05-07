package com.enableets.edu.pakage.manager.etm.service;


import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.etm.bo.EtmBookInfoBO;
import com.enableets.edu.pakage.manager.etm.bo.EtmBookPageBO;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMPageDTO;
import com.enableets.edu.sdk.pakage.etm.dto.EditETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.QueryETMInfoResultDTO;
import com.enableets.edu.sdk.pakage.etm.service.IETMInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EtmInfoService {


    @Autowired
    private IETMInfoService etmInfoServiceSDK;


    /**
     * User Info Service
     */
    @Autowired
    private BaseInfoService baseInfoService;

    public EtmBookInfoBO add(EtmBookInfoBO etmBookInfoBO) {
        etmBookInfoBO.setUser(BeanUtils.convert(baseInfoService.getUserInfo(etmBookInfoBO.getUser().getId()), IdNameMapBO.class));
        QueryETMInfoResultDTO queryETMInfoResultDTO = null;
        if (etmBookInfoBO.getContentId() == null) {
            queryETMInfoResultDTO = etmInfoServiceSDK.addETMInfo(BeanUtils.convert(etmBookInfoBO, AddETMInfoDTO.class));
        } else {
            queryETMInfoResultDTO = etmInfoServiceSDK.editETMInfo(BeanUtils.convert(etmBookInfoBO, EditETMInfoDTO.class));
        }
        return BeanUtils.convert(queryETMInfoResultDTO, EtmBookInfoBO.class);
    }

    public EtmBookInfoBO parse(Long contentId) {
        QueryETMInfoResultDTO queryETMInfoResultDTO = etmInfoServiceSDK.parseETMInfo(contentId);
        return BeanUtils.convert(queryETMInfoResultDTO, EtmBookInfoBO.class);
    }

    public EtmBookInfoBO edit(EtmBookPageBO etmBookPageBO) {
        QueryETMInfoResultDTO queryETMInfoResultDTO = etmInfoServiceSDK.editOnePage(BeanUtils.convert(etmBookPageBO, AddETMPageDTO.class));
        return BeanUtils.convert(queryETMInfoResultDTO, EtmBookInfoBO.class);
    }

    public Boolean delete(String pageInfoId) {
        Boolean result = etmInfoServiceSDK.deleteOnePage(pageInfoId);
        return result;
    }

    public EtmBookInfoBO query(String bookId) {
        QueryETMInfoResultDTO queryETMInfoResultDTO = etmInfoServiceSDK.queryETMInfo(bookId);
        return BeanUtils.convert(queryETMInfoResultDTO, EtmBookInfoBO.class);
    }

}
