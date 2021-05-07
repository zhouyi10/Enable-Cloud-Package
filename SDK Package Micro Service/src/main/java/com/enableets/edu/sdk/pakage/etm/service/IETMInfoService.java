package com.enableets.edu.sdk.pakage.etm.service;


import com.enableets.edu.sdk.pakage.etm.dto.AddETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMPageDTO;
import com.enableets.edu.sdk.pakage.etm.dto.EditETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.QueryETMInfoResultDTO;

public interface IETMInfoService {

    public QueryETMInfoResultDTO addETMInfo(AddETMInfoDTO addETMInfoDTO);

    public QueryETMInfoResultDTO parseETMInfo(Long contentId);

    public QueryETMInfoResultDTO editETMInfo(EditETMInfoDTO editETMInfoDTO);

    public QueryETMInfoResultDTO editOnePage(AddETMPageDTO addETMPageDTO);

    public Boolean deleteOnePage(String pageInfoId);

    public QueryETMInfoResultDTO queryETMInfo(String bookId);
}
