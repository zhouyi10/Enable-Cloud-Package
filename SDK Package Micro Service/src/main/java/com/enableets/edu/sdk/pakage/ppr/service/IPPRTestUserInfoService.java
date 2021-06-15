package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.*;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/19
 **/
public interface IPPRTestUserInfoService {

    public List<QueryUserAnswerTrackResultDTO> getAnswerTracks(String testUserId);

    public List<QueryTestUserResultDTO> queryAnswer(QueryAnswerDTO queryAnswerDTO);

    public TestMarkResultInfoDTO mark(MarkInfoDTO markInfoDTO);

    public void editCanvas(EditCanvasInfoDTO editCanvasInfoDTO);
}
