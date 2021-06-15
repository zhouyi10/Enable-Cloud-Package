package com.enableets.edu.pakage.microservice.assessment.restful;

import com.enableets.edu.framework.core.controller.OperationResult;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.UserAnswerInfoBO;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.TestUserInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.SubmitAnswerV2Service;
import com.enableets.edu.pakage.microservice.ppr.vo.AddTestInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.AnswerCardSubmitInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.MarkInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryTestInfoResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Api(value = "Assessment Action Flow API", tags = "Assessment Action Flow API")
@RestController
@RequestMapping(value = "/microservice/packageservice/assessment")
public class AssessmentActionFlowRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private TestInfoService testInfoService;

    @Autowired
    private SubmitAnswerV2Service submitAnswerV2Service;

    @Autowired
    private TestUserInfoService testUserInfoService;

    @ApiOperation(value = "Assessment publish", notes = "Assessment publish")
    @PostMapping(value="/publish")
    public Response<QueryTestInfoResultVO> publish(
            @ApiParam(value = "Add Test Param", required = true) @RequestBody AddTestInfoVO addTestInfoVO) {
        TestInfoBO test = testInfoService.add(BeanUtils.convert(addTestInfoVO, TestInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(test, QueryTestInfoResultVO.class));
    }


    @ApiOperation(value = "Query Publish Assessment", notes = "Query User Publish Assessment")
    @GetMapping(value = "/{businessId}")
    public Response<QueryTestInfoResultVO> queryTest(
            @ApiParam(value = "businessId", required = true) @PathVariable("businessId") String businessId){
        TestInfoBO testInfoBO = testInfoService.get(businessId);
        return responseTemplate.format(BeanUtils.convert(testInfoBO, QueryTestInfoResultVO.class));
    }

    @ApiOperation(value = "Assessment Answer", notes = "Assessment Recipient Submit AnswerCard")
    @RequestMapping(value = "/test/submit", method = RequestMethod.POST)
    public Response<String> submitAnswer(@ApiParam(value = "Assessment Submit VO", required = true)@RequestBody AnswerCardSubmitInfoVO answerCardInfoVO){
        OperationResult result = submitAnswerV2Service.save(answerCardInfoVO.getEnableCardXml());
        if (result.isSuccess()) return responseTemplate.format(result.getData().toString());
        return responseTemplate.format(null);
    }

    @ApiOperation(value = "mark", notes = "mark")
    @PostMapping(value = "/mark")
    public Response<Boolean> mark(@ApiParam(value = "Mark Info", required = true) @RequestBody MarkInfoVO markInfoVO) {
        testUserInfoService.mark(markInfoVO.getTestId(), markInfoVO.getType(), com.enableets.edu.framework.core.util.BeanUtils.convert(markInfoVO.getAnswers(), UserAnswerInfoBO.class));
        return responseTemplate.format(Boolean.TRUE);
    }
}
