package com.enableets.edu.sdk.pakage.ppr.impl;

import com.enableets.edu.sdk.pakage.ppr.dto.AddTestInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryQuestionAssignmentMarkProgressResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryQuestionAssignmentResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryTestInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QuestionAssignmentDTO;
import com.enableets.edu.sdk.pakage.ppr.feign.IPPRTestInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRTestInfoService;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
public class DefaultPPRTestInfoService implements IPPRTestInfoService {

    private IPPRTestInfoServiceFeignClient pprTestInfoServiceFeignClient;

    public DefaultPPRTestInfoService(IPPRTestInfoServiceFeignClient pprTestInfoServiceFeignClient){
        this.pprTestInfoServiceFeignClient = pprTestInfoServiceFeignClient;
    }

    @Override
    public QueryTestInfoResultDTO get(String testId) {
        return this.get(testId, null, null, null);
    }

    @Override
    public QueryTestInfoResultDTO get(String testId, String stepId, String fileId, String examId) {
        return pprTestInfoServiceFeignClient.get(testId, stepId, fileId, examId).getData();
    }

    @Override
    public QueryTestInfoResultDTO add(AddTestInfoDTO addTestInfoDTO) {
        return pprTestInfoServiceFeignClient.add(addTestInfoDTO).getData();
    }

    @Override
    public List<QueryQuestionAssignmentResultDTO> queryAssign(String testId, String userId) {
        return pprTestInfoServiceFeignClient.queryAssign(testId, userId).getData();
    }

    @Override
    public List<QueryQuestionAssignmentMarkProgressResultDTO> queryMarkedProgress(String stepId, String fileId) {
        return pprTestInfoServiceFeignClient.queryMarkedProgress(stepId, fileId).getData();
    }

    @Override
    public boolean addTestAssignerTeacher(List<QuestionAssignmentDTO> assignments) {
        return pprTestInfoServiceFeignClient.addTestAssignerTeacher(assignments).getData();
    }
}
