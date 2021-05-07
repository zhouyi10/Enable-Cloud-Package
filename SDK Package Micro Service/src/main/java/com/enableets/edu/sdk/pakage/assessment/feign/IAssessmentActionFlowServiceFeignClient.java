package com.enableets.edu.sdk.pakage.assessment.feign;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentActionFlowDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentMarkStatusDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.AssessmentSubmitDTO;
import com.enableets.edu.sdk.pakage.assessment.dto.QueryAssessmentActionFlowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author: Bill_Liu@enable-ets.com
 * @Date: 2021/3/24 16:49
 */
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IAssessmentActionFlowServiceFeignClient {

    @RequestMapping(value = "/microservice/packageservice/assessment/publish", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowDTO> publish(@RequestBody AssessmentActionFlowDTO assessmentActionFlowDTO);

    @RequestMapping(value = "/microservice/packageservice/assessment/publish", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowDTO>> queryPublishList(@RequestParam(value = "teacherId", required = true) String teacherId);

    @RequestMapping(value = "/microservice/packageservice/assessment/test", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowDTO>> queryTest(@RequestParam(value = "studentId", required = true)String studentId);

    @RequestMapping(value = "/microservice/packageservice/assessment/test/submit", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowDTO> submitAnswer(@RequestBody AssessmentSubmitDTO assessmentSubmitDTO);

    @RequestMapping(value = "/microservice/packageservice/assessment/mark", method = RequestMethod.GET)
    public Response<List<AssessmentMarkStatusDTO>> queryMyMark(@RequestParam(value = "teacherId", required = true)String teacherId, @RequestParam(value = "offset", required = true)int offset, @RequestParam(value = "rows", required = true)int rows);

    @RequestMapping(value = "/microservice/packageservice/assessment/mark", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowDTO> submitMark(@RequestBody AssessmentSubmitDTO assessmentSubmitDTO);

    @RequestMapping(value = "/microservice/packageservice/assessment/report", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowDTO>> queryReprot(@RequestParam(value = "studentId", required = true)String studentId);

    @RequestMapping(value = "/microservice/packageservice/assessment/report", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowDTO> studentReprot(@RequestBody AssessmentSubmitDTO assessmentSubmitDTO);
}
