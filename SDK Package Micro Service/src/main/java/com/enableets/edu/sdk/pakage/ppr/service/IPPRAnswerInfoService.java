package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.core.MicroServiceException;
import com.enableets.edu.sdk.pakage.ppr.dto.AnswerCardSubmitInfoDTO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/19
 **/
public interface IPPRAnswerInfoService {

    public String submit(AnswerCardSubmitInfoDTO answerCardSubmitInfoDTO);
}
