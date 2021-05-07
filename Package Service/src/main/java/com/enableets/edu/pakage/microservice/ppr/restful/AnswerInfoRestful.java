package com.enableets.edu.pakage.microservice.ppr.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.test.service.TestInfoService;
import com.enableets.edu.pakage.framework.ppr.test.service.submit.SubmitAnswerService;
import com.enableets.edu.pakage.framework.ppr.bo.TestInfoBO;
import com.enableets.edu.pakage.microservice.ppr.vo.AnswerSubmitInfoVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
    public SubmitAnswerService submitAnswerService;

    @Autowired
    public TestInfoService testInfoService;

    /**
     * Submit Answer & Temporary storage
     * @param answerSubmitInfoVO
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Response<String> submit(@RequestBody AnswerSubmitInfoVO answerSubmitInfoVO){
        String testUserId = submitAnswerService.save(answerSubmitInfoVO.getEnableCardXml());
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
