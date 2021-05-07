package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.AddTestInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryQuestionAssignmentMarkProgressResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryQuestionAssignmentResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QuestionAssignmentDTO;

import java.util.List;

/**
 * PPR Test Related interface
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
public interface IPPRTestInfoService {

    public QueryTestInfoResultDTO get(String testId, String stepId, String fileId, String examId);

    public QueryTestInfoResultDTO add(AddTestInfoDTO addTestInfoDTO);

    public List<QueryQuestionAssignmentResultDTO> queryAssign(String testId, String userId);

    public List<QueryQuestionAssignmentMarkProgressResultDTO> queryMarkedProgress(String stepId, String fileId);

    public boolean addTestAssignerTeacher(List<QuestionAssignmentDTO> assignments);
}
