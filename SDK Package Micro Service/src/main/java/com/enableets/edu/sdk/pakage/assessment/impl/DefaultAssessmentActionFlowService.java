package com.enableets.edu.sdk.pakage.assessment.impl;

import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentActionFlowDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentMarkStatusDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentSubmitDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.QueryAssessmentActionFlowDTO;
import com.enableets.edu.sdk.pakage.assessment.feign.IAssessmentActionFlowServiceFeignClient;
import com.enableets.edu.sdk.pakage.assessment.service.IAssessmentActionFlowService;

import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/24 16:47
 */
public class DefaultAssessmentActionFlowService implements IAssessmentActionFlowService{

    private IAssessmentActionFlowServiceFeignClient assessmentActionFlowServiceFeignClient;

    public DefaultAssessmentActionFlowService(IAssessmentActionFlowServiceFeignClient assessmentActionFlowServiceFeignClient) {
        this.assessmentActionFlowServiceFeignClient = assessmentActionFlowServiceFeignClient;
    }

    @Override
    public QueryAssessmentActionFlowDTO publish(AssessmentActionFlowDTO assessmentActionFlowDTO) {
        return assessmentActionFlowServiceFeignClient.publish(assessmentActionFlowDTO).getData();
    }

    @Override
    public List<QueryAssessmentActionFlowDTO> queryPublishList(String teacherId) {
        return assessmentActionFlowServiceFeignClient.queryPublishList(teacherId).getData();
    }

    @Override
    public List<QueryAssessmentActionFlowDTO> queryTest(String studentId) {
        return assessmentActionFlowServiceFeignClient.queryTest(studentId).getData();
    }

    @Override
    public QueryAssessmentActionFlowDTO submitAnswer(AssessmentSubmitDTO assessmentSubmitDTO) {
        return assessmentActionFlowServiceFeignClient.submitAnswer(assessmentSubmitDTO).getData();
    }

    @Override
    public List<AssessmentMarkStatusDTO> queryMyMark(String teacherId, int offset, int rows) {
        return assessmentActionFlowServiceFeignClient.queryMyMark(teacherId,offset,rows).getData();
    }

    @Override
    public QueryAssessmentActionFlowDTO submitMark(AssessmentSubmitDTO assessmentSubmitDTO) {
        return assessmentActionFlowServiceFeignClient.submitMark(assessmentSubmitDTO).getData();
    }

    @Override
    public List<QueryAssessmentActionFlowDTO> queryReprot(String studentId) {
        return assessmentActionFlowServiceFeignClient.queryReprot(studentId).getData();
    }

    @Override
    public QueryAssessmentActionFlowDTO studentReprot(AssessmentSubmitDTO assessmentSubmitDTO) {
        return assessmentActionFlowServiceFeignClient.studentReprot(assessmentSubmitDTO).getData();
    }
}
