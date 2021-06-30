package com.enableets.edu.pakage.microservice.ppr.restful;

import com.enableets.edu.pakage.framework.ppr.test.bo.TestMarkResultInfoBO;
import com.enableets.edu.pakage.microservice.ppr.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.test.service.TestUserInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.QueryMarkAnswerBO;
import com.enableets.edu.pakage.framework.ppr.bo.TestUserInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerCanvasInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerStampBO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/19
 **/
@Api(value = "(4)PPR Test User Api", tags = "(4)Paper Test User Info Api", position = 4)
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr/tests/users")
public class TestUserInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private TestUserInfoService testUserInfoService;

    @ApiOperation(value = "GET Answer Track", notes = "Get Answer Question track")
    @GetMapping(value = "/{testUserId}/answer/track")
    public Response<List<QueryUserAnswerTrackResultVO>> getAnswerTracks(@PathVariable("testUserId") String testUserId){
        List<UserAnswerStampBO> answerTracks = testUserInfoService.getAnswerTracks(testUserId);
        return responseTemplate.format(BeanUtils.convert(answerTracks, QueryUserAnswerTrackResultVO.class));
    }

    @ApiOperation(value = "Query Answers", notes = "Query TestUser include answers")
    @GetMapping(value = "/answers")
    public Response<List<QueryTestUserResultVO>> queryAnswer(@ApiParam(value = "Test ID", required = false) @RequestParam(value = "testId", required = false) String testId,
                                                             @ApiParam(value = "Step ID", required = false) @RequestParam(value = "stepId", required = false) String stepId,
                                                             @ApiParam(value = "File ID", required = false) @RequestParam(value = "fileId", required = false) String fileId,
                                                             @ApiParam(value = "Exam ID", required = false) @RequestParam(value = "examId", required = false) String examId,
                                                             @ApiParam(value = "User ID", required = false) @RequestParam(value = "userId", required = false) String userId,
                                                             @ApiParam(value = "Group IDs", required = false) @RequestParam(value = "groupIds", required = false) String groupIds,
                                                             @ApiParam(value = "Question IDs", required = false) @RequestParam(value = "questionIds", required = false) String questionIds){
        QueryMarkAnswerBO answerBO = new QueryMarkAnswerBO();
        answerBO.setTestId(testId);
        answerBO.setStepId(stepId);
        answerBO.setFileId(fileId);
        answerBO.setExamId(examId);
        answerBO.setUserId(userId);
        answerBO.setGroupIds(groupIds);
        answerBO.setQuestionIds(questionIds);
        List<TestUserInfoBO> testUsers = testUserInfoService.queryAnswer(answerBO);
        return responseTemplate.format(BeanUtils.convert(testUsers, QueryTestUserResultVO.class));
    }

    @ApiOperation(value = "Do Mark", notes = "Do Mark")
    @PostMapping(value = "/mark")
    public Response<TestMarkResultInfoVO> mark(@ApiParam(value = "Mark Info", required = true) @RequestBody MarkInfoVO markInfoVO){
        TestMarkResultInfoBO result = testUserInfoService.mark(markInfoVO.getTestId(), markInfoVO.getType(), BeanUtils.convert(markInfoVO.getAnswers(), UserAnswerInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(result, TestMarkResultInfoVO.class));
    }

    @ApiOperation(value = "Edit Canvas", notes = "Edit Canvas")
    @PutMapping(value = "/answer/canvas")
    public Response<Boolean> editCanvas(@ApiParam(value = "Edit Canvas Param", required = true) @RequestBody EditCanvasInfoVO editCanvasInfoVO){
        testUserInfoService.editCanvasInfo(BeanUtils.convert(editCanvasInfoVO, UserAnswerCanvasInfoBO.class));
        return responseTemplate.format(Boolean.TRUE);
    }
}
