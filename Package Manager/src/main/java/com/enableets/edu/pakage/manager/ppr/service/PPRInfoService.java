package com.enableets.edu.pakage.manager.ppr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.manager.ppr.bo.PaperInfoBO;
import com.enableets.edu.sdk.pakage.ppr.dto.AddPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.EditPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPPRInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRAnswerCardInfoService;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRInfoService;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
@Service
public class PPRInfoService {

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private IPPRInfoService pprInfoServiceSDK;

    @Autowired
    private IPPRAnswerCardInfoService pprAnswerCardServiceSDK;

    /**
     * Add PPR
     * @param paperInfoBO
     * @return
     */
    public PaperInfoBO save(PaperInfoBO paperInfoBO) {
        paperInfoBO.setUser(BeanUtils.convert(baseInfoService.getUserInfo(paperInfoBO.getUserId()), IdNameMapBO.class));
        paperInfoBO.setSchool(BeanUtils.convert(baseInfoService.getUserSchoolInfo(paperInfoBO.getUserId()), IdNameMapBO.class));
        QueryPPRInfoResultDTO pprInfo = null;
        if (paperInfoBO.getPaperId() == null) {
            pprInfo = pprInfoServiceSDK.add(BeanUtils.convert(paperInfoBO, AddPPRInfoDTO.class));
        } else {
            paperInfoBO.setContentId(paperInfoBO.getPaperId());
            pprInfo = pprInfoServiceSDK.edit(BeanUtils.convert(paperInfoBO, EditPPRInfoDTO.class));
        }
        return BeanUtils.convert(pprInfo, PaperInfoBO.class);
    }

    public PaperInfoBO get(String paperId){
        QueryPPRInfoResultDTO pprInfoResultDTO = pprInfoServiceSDK.get(paperId);
        if (pprInfoResultDTO != null){
            PaperInfoBO paper = BeanUtils.convert(pprInfoResultDTO, PaperInfoBO.class);
            paper.setAnswerCard(BeanUtils.convert(pprAnswerCardServiceSDK.getAnswerCard(paperId, null), AnswerCardInfoBO.class));
            return paper;
        }
        return null;
    }
}
