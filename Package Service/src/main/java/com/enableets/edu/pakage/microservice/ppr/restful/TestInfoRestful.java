package com.enableets.edu.pakage.microservice.ppr.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionAssignmentMarkedProcessBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.microservice.ppr.vo.AddTestInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryQuestionAssignmentMarkProgressResultVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryQuestionAssignmentResultVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryTestInfoResultVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QuestionAssignmentVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * Test Info Service
 * @author walle_yu@enable-ets.com
 * @since 2020/08/20
 **/
@Api(value = "[3]PPR Test Api", tags = "Paper Test Info Api", position = 3)
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr")
public class TestInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private TestInfoService testInfoService;

    @ApiOperation(value = "Get Test Information", notes = "Get Test Information")
    @RequestMapping(value = "/tests/test", method = RequestMethod.GET)
    public Response<QueryTestInfoResultVO> get(String testId, String stepId, String fileId, String examId){
        TestInfoBO testInfoBO = testInfoService.get(testId, stepId, fileId, examId, null);
        return responseTemplate.format(BeanUtils.convert(testInfoBO, QueryTestInfoResultVO.class));
    }

    /**
     * Add Test
     * @param addTestInfoVO
     * @return
     */
    @ApiOperation(value = "Add Test Information", notes = "Add Test Information")
    @RequestMapping(value = "/tests/test", method = RequestMethod.POST)
    public Response<QueryTestInfoResultVO> add(@ApiParam(value = "Add Test Param", required = true) @RequestBody AddTestInfoVO addTestInfoVO){
        TestInfoBO testInfoBO = testInfoService.add(BeanUtils.convert(addTestInfoVO, TestInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(testInfoBO, QueryTestInfoResultVO.class));
    }

    /**
     * Designated exam review completed
     * @param testId
     * @return
     */
    @ApiOperation(value = "Do Test Complete", notes = "Do Test Complete")
    @RequestMapping(value = "/tests/{testId}/mark/complete", method = RequestMethod.PUT)
    public Response<Boolean> testCompleteMark(@PathVariable(value = "testId") String testId){
        testInfoService.complete(testId);
        return responseTemplate.format(Boolean.TRUE);
    }

    /**
     * Query user assigned review information
     * @param testId
     * @param userId
     * @return
     */
    @ApiOperation(value = "Query user assigned review information", notes = "Query the user's assigned review information (including reviewing students)")
    @RequestMapping(value = "/tests/test/{testId}/assign", method = RequestMethod.GET)
    public Response<List<QueryQuestionAssignmentResultVO>> queryAssign(@ApiParam(value = "Test ID", required = true) @PathVariable("testId") String testId, @ApiParam(value = "User ID", required = false) @RequestParam(value = "userId", required = false) String userId){
        List<QuestionAssignmentBO> assignments = testInfoService.queryAssign(testId, userId);
        return responseTemplate.format(BeanUtils.convert(assignments, QueryQuestionAssignmentResultVO.class));
    }

    /**
     * Query exam review progress
     * @param stepId
     * @param fileId
     * @return
     */
    @ApiOperation(value = "Query exam review progress", notes = "Query exam review progress")
    @RequestMapping(value = "/tests/marked/progress", method = RequestMethod.GET)
    public Response<List<QueryQuestionAssignmentMarkProgressResultVO>> queryMarkedProgress(@ApiParam(value = "Step ID", required = true) @RequestParam(value = "stepId", required = true) String stepId, @ApiParam(value = "File ID", required = true) @RequestParam(value = "fileId", required = true) String fileId){
        List<QuestionAssignmentMarkedProcessBO> markedProcesses = testInfoService.queryMarkedProgress(stepId, fileId);
        return responseTemplate.format(BeanUtils.convert(markedProcesses, QueryQuestionAssignmentMarkProgressResultVO.class));
    }

    /**
     * Add or edit assigned review teachers
     * @param list
     * @return
     */
    @ApiOperation(value = "Add or edit assigned review teachers", notes = "Add or edit assigned review teachers")
    @RequestMapping(value = "/tests/mark/assign", method = RequestMethod.POST)
    public Response<Boolean> addTestAssignerTeacher(@ApiParam(value = "Assign Information", required = true) @RequestBody List<QuestionAssignmentVO> list){
        testInfoService.addTestAssignerTeacher(BeanUtils.convert(list, QuestionAssignmentBO.class));
        return responseTemplate.format(Boolean.TRUE);
    }
}
