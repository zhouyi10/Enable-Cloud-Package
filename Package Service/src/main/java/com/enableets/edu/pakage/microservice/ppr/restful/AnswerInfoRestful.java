package com.enableets.edu.pakage.microservice.ppr.restful;

import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.SubmitAnswerV2Service;
import com.enableets.edu.pakage.framework.ppr.test.service.submitV2.processor.AnswerRequestCompressorRunner;
import com.enableets.edu.pakage.microservice.ppr.vo.AnswerCardSubmitInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * answer restful interface
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/
@Api(value = "[5]Test answer Info Api", tags = "answer、submit 、mark info!", position = 5)
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr/answer")
public class AnswerInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    public SubmitAnswerV2Service submitAnswerV2Service;

    @Autowired
    public TestInfoService testInfoService;

    @Autowired
    private AnswerRequestCompressorRunner answerRequestCompressorRunner;

    @Autowired
    private AnswerInfoService answerInfoService;

    /**
     * Submit Answer & Temporary storage
     * @param answerSubmitInfoVO
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Response<String> submit(@ApiParam(value = "Answer Card Info", required = true) @RequestBody AnswerCardSubmitInfoVO answerSubmitInfoVO){
        String testUserId = (String)submitAnswerV2Service.save(answerSubmitInfoVO.getEnableCardXml()).getData();
        return responseTemplate.format(testUserId);
    }

    /**
     * Get Test Info By ID
     * @param testId Test ID
     * @return TestInfoBO
     */
    @ApiOperation(value = "Get Test Info", notes = "Get Test Info By ID")
    @RequestMapping(value = "/test/{testId}", method = RequestMethod.GET)
    public Response<TestInfoBO> get(@ApiParam(value = "Test ID", required = true) @PathVariable("testId") String testId){
        return responseTemplate.format(testInfoService.get(testId, null, null, null, Boolean.FALSE));
    }
}
