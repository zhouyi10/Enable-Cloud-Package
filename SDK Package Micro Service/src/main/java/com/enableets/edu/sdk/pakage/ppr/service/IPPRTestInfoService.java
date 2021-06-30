package com.enableets.edu.sdk.pakage.ppr.service;

import com.enableets.edu.sdk.pakage.ppr.dto.*;

import java.util.List;

/**
 * PPR Test Related interface
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
public interface IPPRTestInfoService {

    public QueryTestInfoResultDTO get(String testId);

    public QueryTestInfoResultDTO get(String testId, String stepId, String fileId, String examId);

    public QueryTestInfoResultDTO add(AddTestInfoDTO addTestInfoDTO);

    public List<QueryQuestionAssignmentResultDTO> queryAssign(String testId, String userId);

    public List<QueryQuestionAssignmentMarkProgressResultDTO> queryMarkedProgress(String stepId, String fileId);

    public boolean addTestAssignerTeacher(List<QuestionAssignmentDTO> assignments);

    public List<TeacherTestResultDTO> queryResultForTeacher(QueryTeacherTestDTO teacherDTO);

    public Integer countResultForTeacher(QueryTeacherTestDTO teacherDTO);
}
