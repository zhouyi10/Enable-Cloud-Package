package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.AddAnswerCardInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
public interface IPPRAnswerCardInfoService {

    public QueryAnswerCardInfoResultDTO getAnswerCard(String paperId, String creator);

    public QueryAnswerCardInfoResultDTO add(AddAnswerCardInfoDTO answerCardInfo);
    /**
     * 根据条件查询答题卡信息
     * @param examId 试卷标识
     * @param creator 创建人标识
     * @return 答题卡信息
     */
    public List<QueryAnswerCardInfoResultDTO> query(Long examId, String creator);

}
