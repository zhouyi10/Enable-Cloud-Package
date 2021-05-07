package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.AddPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.EditPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPPRInfoResultDTO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
public interface IPPRInfoService {

    public QueryPPRInfoResultDTO add(AddPPRInfoDTO addPPRInfoDTO);

    public QueryPPRInfoResultDTO get(String id);

    public QueryPPRInfoResultDTO edit(EditPPRInfoDTO editPPRInfoDTO);
}
