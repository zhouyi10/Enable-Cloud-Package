package com.enableets.edu.pakage.manager.ppr.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.pakage.manager.mark.bo.KnowledgeInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.AnswerCardAxisBO;
import com.enableets.edu.pakage.manager.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.ResourceBaseInfoBO;
import com.enableets.edu.sdk.pakage.ppr.dto.AddAnswerCardInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRAnswerCardInfoService;
import com.enableets.edu.sdk.paper.dto.*;
import com.enableets.edu.sdk.paper.service.IQuestionInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author duffy_ding
 * @since 2019/11/06
 */
@Service
public class AnswerCardService {

    /** 答题卡信息接口sdk */
    @Autowired
    private IPPRAnswerCardInfoService pprAnswerCardInfoServiceSDK;

    @Autowired
    private IQuestionInfoService questionInfoServiceSDK;

    /**
     * 获取答题卡信息
     * @param paperId 试卷标识
     * @param creator 用户标识
     * @return 答题卡信息
     */
    public AnswerCardInfoBO getAnswerCardInfo(Long paperId, String creator) {
        List<QueryAnswerCardInfoResultDTO> answerCardInfos = pprAnswerCardInfoServiceSDK.query(paperId, creator);
        if (CollectionUtils.isEmpty(answerCardInfos)) {
            return null;
        }
        return BeanUtils.convert(answerCardInfos.get(0), AnswerCardInfoBO.class);
    }

    /**
     * 新增答题卡信息
     * @param cardInfo 答题卡信息
     * @return 答题卡信息
     */
    public AnswerCardInfoBO addAnswerCardInfo(AnswerCardInfoBO cardInfo) {
        AddAnswerCardInfoDTO param = BeanUtils.convert(cardInfo, AddAnswerCardInfoDTO.class);
        QueryAnswerCardInfoResultDTO result = pprAnswerCardInfoServiceSDK.add(param);
        return BeanUtils.convert(result, AnswerCardInfoBO.class);
    }
}
