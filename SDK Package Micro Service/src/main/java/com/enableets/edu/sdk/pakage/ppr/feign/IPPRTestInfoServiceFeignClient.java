package com.enableets.edu.sdk.pakage.ppr.feign;

import com.enableets.edu.sdk.pakage.ppr.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enableets.edu.sdk.core.Response;

import java.util.List;

/**
 * Package-PPR Test Client
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRTestInfoServiceFeignClient {

    /**
     * Query Test Information
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/test", method = RequestMethod.GET)
    public Response<QueryTestInfoResultDTO> get(@RequestParam(value = "testId", required = false) String testId, @RequestParam(value = "stepId", required = false) String stepId, @RequestParam(value = "fileId", required = false) String fileId, @RequestParam(value = "examId", required = false) String examId);

    /**
     * New Add Test
     * @param addTestInfoDTO
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/test", method = RequestMethod.POST)
    public Response<QueryTestInfoResultDTO> add(@RequestBody AddTestInfoDTO addTestInfoDTO);

    /**
     * Check the assignment of the Test
     * @param testId
     * @param userId
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/test/{testId}/assign", method = RequestMethod.GET)
    public Response<List<QueryQuestionAssignmentResultDTO>> queryAssign(@PathVariable("testId") String testId, @RequestParam(value = "userId", required = false) String userId);

    /**
     * Check the review progress of the Test
     * @param stepId
     * @param fileId
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/marked/progress", method = RequestMethod.GET)
    public Response<List<QueryQuestionAssignmentMarkProgressResultDTO>> queryMarkedProgress(@RequestParam(value = "stepId", required = true) String stepId, @RequestParam(value = "fileId", required = true) String fileId);

    /**
     * Add assignment
     * @param assignments
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/mark/assign", method = RequestMethod.POST)
    public Response<Boolean> addTestAssignerTeacher(@RequestBody List<QuestionAssignmentDTO> assignments);


    /**
     * Query Test Information
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/teacher", method = RequestMethod.POST)
    public Response<List<TeacherTestResultDTO>> queryResultForTeacher(@RequestBody QueryTeacherTestDTO teacherDTO);


    /**
     * Query Test Count
     * @return
     */
    @RequestMapping(value = "/microservice/packageservice/ppr/tests/count", method = RequestMethod.POST)
    public Response<Integer> countResultForTeacher(@RequestBody QueryTeacherTestDTO teacherDTO);

}
