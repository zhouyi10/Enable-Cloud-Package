package com.enableets.edu.sdk.pakage.assessment.service;

import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentActionFlowDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentMarkStatusDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentSubmitDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.QueryAssessmentActionFlowDTO;

import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/24 16:31
 */
public interface IAssessmentActionFlowService {

    public QueryAssessmentActionFlowDTO publish(AssessmentActionFlowDTO assessmentActionFlowDTO);

    public List<QueryAssessmentActionFlowDTO> queryPublishList(String teacherId);

    public List<QueryAssessmentActionFlowDTO> queryTest(String studentId);

    public QueryAssessmentActionFlowDTO submitAnswer(AssessmentSubmitDTO assessmentSubmitDTO);

    public List<AssessmentMarkStatusDTO> queryMyMark(String teacherId, int offset , int rows);

    public QueryAssessmentActionFlowDTO submitMark(AssessmentSubmitDTO assessmentSubmitDTO);

    public List<QueryAssessmentActionFlowDTO> queryReprot(String studentId);

    public QueryAssessmentActionFlowDTO studentReprot(AssessmentSubmitDTO assessmentSubmitDTO);


}
