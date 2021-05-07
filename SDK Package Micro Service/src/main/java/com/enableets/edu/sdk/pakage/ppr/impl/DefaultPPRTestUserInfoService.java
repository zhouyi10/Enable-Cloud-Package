package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.pakage.ppr.dto.EditCanvasInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.MarkInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestUserResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryUserAnswerTrackResultDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRTestInfoUserServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRTestUserInfoService;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/19
 **/
public class DefaultPPRTestUserInfoService implements IPPRTestUserInfoService {

    private IPPRTestInfoUserServiceFeignClient pprTestInfoUserServiceFeignClient;

    public DefaultPPRTestUserInfoService(IPPRTestInfoUserServiceFeignClient pprTestInfoUserServiceFeignClient){
        this.pprTestInfoUserServiceFeignClient = pprTestInfoUserServiceFeignClient;
    }

    @Override
    public List<QueryUserAnswerTrackResultDTO> getAnswerTracks(String testUserId) {
        return pprTestInfoUserServiceFeignClient.getAnswerTracks(testUserId).getData();
    }

    @Override
    public List<QueryTestUserResultDTO> queryAnswer(QueryAnswerDTO queryAnswerDTO) {
        return pprTestInfoUserServiceFeignClient.queryAnswer(queryAnswerDTO.getTestId(), queryAnswerDTO.getStepId(), queryAnswerDTO.getFileId(), queryAnswerDTO.getExamId(), queryAnswerDTO.getUserId(), queryAnswerDTO.getGroupIds(), queryAnswerDTO.getQuestionIds()).getData();
    }

    @Override
    public void mark(MarkInfoDTO markInfoDTO) {
        pprTestInfoUserServiceFeignClient.mark(markInfoDTO);
    }

    @Override
    public void editCanvas(EditCanvasInfoDTO editCanvasInfoDTO) {
        pprTestInfoUserServiceFeignClient.editCanvas(editCanvasInfoDTO);
    }
}
