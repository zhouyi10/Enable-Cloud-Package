package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.AddPaperInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPaperInfoResultDTO;

/**
 * PPR Paper Related interface
 * @author walle_yu@enable-ets.com
 * @since 2020/09/09
 **/
public interface IPPRPaperInfoService {

    public QueryPaperInfoResultDTO get(Long paperId);

    public QueryPaperInfoResultDTO add(AddPaperInfoDTO addPaperInfoDTO);
}
