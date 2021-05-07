package com.enableets.edu.pakage.microservice.actionflow.restful;

import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.framework.actionflow.bo.AssessmentActionFlowBO;
import com.enableets.edu.pakage.framework.actionflow.bo.AssessmentMarkStatusBO;
import com.enableets.edu.pakage.framework.actionflow.service.AssessmentService;
import com.enableets.edu.pakage.microservice.actionflow.vo.AssessmentActionFlowVO;
import com.enableets.edu.pakage.microservice.actionflow.vo.AssessmentMarkStatusVO;
import com.enableets.edu.pakage.microservice.actionflow.vo.QueryAssessmentActionFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "Action Flow API", tags = "Action Flow API")
@RestController
@RequestMapping(value = "/microservice/packageservice/actionflow/assessment/")
public class AssessmentProcessRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private AssessmentService assessmentService;

    @ApiOperation(value = "start assessment", notes = "start assessment")
    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowVO> start(
            @ApiParam(value = "Action Flow Info", required = true) @RequestBody AssessmentActionFlowVO assessmentActionFlowVO) {
        AssessmentActionFlowBO bo = assessmentService.start(BeanUtils.convert(assessmentActionFlowVO, AssessmentActionFlowBO.class));
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "query assessment", notes = "query assessment")
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowVO>> queryMyAssessment(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId){

        List<AssessmentActionFlowBO> bos = assessmentService.queryTestList(userId);
        return responseTemplate.format(BeanUtils.convert(bos,QueryAssessmentActionFlowVO.class));
    }


    @ApiOperation(value = "submit answer", notes = "submit answer")
    @RequestMapping(value = "/submit/answer", method = RequestMethod.GET)
    public Response<QueryAssessmentActionFlowVO> submitAnswer(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId,
            @ApiParam(value = "processInstanceId", required = true) @RequestParam(value = "processInstanceId", required = true) String processInstanceId
    ){
        AssessmentActionFlowBO bo = assessmentService.submitAnswer(processInstanceId, userId);
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "query mark", notes = "query mark")
    @RequestMapping(value = "/query/mark", method = RequestMethod.GET)
    public Response<List<AssessmentMarkStatusVO>> queryMyMark(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId,
            @ApiParam(value = "offset", required = true) @RequestParam(value = "offset", required = true) int offset,
            @ApiParam(value = "rows", required = true) @RequestParam(value = "rows", required = true) int rows
    ){
        List<AssessmentMarkStatusBO> bos = assessmentService.queryMarkList(userId, offset, rows);
        return responseTemplate.format(BeanUtils.convert(bos, AssessmentMarkStatusVO.class));
    }


    @ApiOperation(value = "mark", notes = "mark")
    @RequestMapping(value = "/submit/mark", method = RequestMethod.GET)
    public Response<QueryAssessmentActionFlowVO> submitMark(
            @ApiParam(value = "senderId", required = true) @RequestParam(value = "senderId", required = true) String senderId,
            @ApiParam(value = "processInstanceId", required = true) @RequestParam(value = "processInstanceId", required = true) String processInstanceId
            ){
        AssessmentActionFlowBO bo = assessmentService.markAnswer(processInstanceId, senderId);
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }


    @ApiOperation(value = "query report", notes = "query report")
    @RequestMapping(value = "/query/report", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowVO>> queryReprot(
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId){
        List<AssessmentActionFlowBO> bos = assessmentService.queryReportList(userId);
        return responseTemplate.format(BeanUtils.convert(bos,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "student report", notes = "student report")
    @RequestMapping(value = "/student/report", method = RequestMethod.GET)
    public Response<QueryAssessmentActionFlowVO> studentReprot(
            @ApiParam(value = "processInstanceId", required = true) @RequestParam(value = "processInstanceId", required = true) String processInstanceId,
            @ApiParam(value = "userId", required = true) @RequestParam(value = "userId", required = true) String userId){
        AssessmentActionFlowBO bo = assessmentService.studentReport(processInstanceId, userId);
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }
}
