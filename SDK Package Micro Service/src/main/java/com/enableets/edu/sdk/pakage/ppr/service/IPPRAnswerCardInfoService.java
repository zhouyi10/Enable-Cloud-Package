package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
public interface IPPRAnswerCardInfoService {

    public QueryAnswerCardInfoResultDTO getAnswerCard(String paperId, String creator);
}
