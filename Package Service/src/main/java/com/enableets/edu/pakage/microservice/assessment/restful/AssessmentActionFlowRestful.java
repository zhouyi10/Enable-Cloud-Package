package com.enableets.edu.pakage.microservice.assessment.restful;

import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.framework.assessment.service.AssessmentActionFlowService;
import com.enableets.edu.pakage.framework.assessment.bo.AssessmentActionFlowBO;
import com.enableets.edu.pakage.microservice.assessment.vo.AssessmentActionFlowVO;
import com.enableets.edu.pakage.microservice.assessment.vo.AssessmentMarkStatusVO;
import com.enableets.edu.pakage.microservice.assessment.vo.AssessmentSubmitVO;
import com.enableets.edu.pakage.microservice.assessment.vo.QueryAssessmentActionFlowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(value = "Assessment Action Flow API", tags = "Assessment Action Flow API")
@RestController
@RequestMapping(value = "/microservice/packageservice/assessment")
public class AssessmentActionFlowRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private AssessmentActionFlowService assessmentActionFlowService;

    @ApiOperation(value = "assessment publish", notes = "assessment publish")
    @RequestMapping(value = "/publish", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowVO> publish(
            @ApiParam(value = "Assessment Action Flow Info", required = true) @RequestBody AssessmentActionFlowVO assessmentActionFlowVO) {
        AssessmentActionFlowBO bo = assessmentActionFlowService.publish(BeanUtils.convert(assessmentActionFlowVO, AssessmentActionFlowBO.class));
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "query assessment publish", notes = "query assessment publish")
    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowVO>> queryPublishList(
            @ApiParam(value = "teacherId", required = true) @RequestParam(value = "teacherId", required = true) String teacherId){
        List<AssessmentActionFlowBO> list = assessmentActionFlowService.queryPublishList(teacherId);
        return responseTemplate.format(BeanUtils.convert(list,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "query test", notes = "query test")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowVO>> queryTest(
            @ApiParam(value = "studentId", required = true) @RequestParam(value = "studentId", required = true) String studentId){

        List<AssessmentActionFlowBO> bos = assessmentActionFlowService.queryTestList(studentId);
        return responseTemplate.format(BeanUtils.convert(bos,QueryAssessmentActionFlowVO.class));
    }


    @ApiOperation(value = "test submit", notes = "test submit")
    @RequestMapping(value = "/test/submit", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowVO> submitAnswer(@ApiParam(value = "Assessment Submit VO", required = true)@RequestBody AssessmentSubmitVO assessmentSubmitVO){
        AssessmentActionFlowBO bo = assessmentActionFlowService.submitAnswer(assessmentSubmitVO.getProcessInstanceId(), assessmentSubmitVO.getUserId());
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "query mark", notes = "query mark")
    @RequestMapping(value = "/mark", method = RequestMethod.GET)
    public Response<List<AssessmentMarkStatusVO>> queryMyMark(
            @ApiParam(value = "teacherId", required = true) @RequestParam(value = "teacherId", required = true) String teacherId,
            @ApiParam(value = "offset", required = true) @RequestParam(value = "offset", required = true) int offset,
            @ApiParam(value = "rows", required = true) @RequestParam(value = "rows", required = true) int rows
    ){
        List<AssessmentActionFlowBO> list = assessmentActionFlowService.queryMarkList(teacherId, offset, rows);
        return responseTemplate.format(BeanUtils.convert(list, AssessmentMarkStatusVO.class));
    }


    @ApiOperation(value = "mark", notes = "mark")
    @RequestMapping(value = "/mark", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowVO> submitMark(
            @ApiParam(value = "Assessment Submit VO", required = true)@RequestBody AssessmentSubmitVO assessmentSubmitVO
            ){
        AssessmentActionFlowBO bo = assessmentActionFlowService.markAnswer(assessmentSubmitVO.getProcessInstanceId(), assessmentSubmitVO.getUserId());
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }


    @ApiOperation(value = "query report", notes = "query report")
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public Response<List<QueryAssessmentActionFlowVO>> queryReprot(
            @ApiParam(value = "studentId", required = true) @RequestParam(value = "studentId", required = true) String studentId){
        List<AssessmentActionFlowBO> bos = assessmentActionFlowService.queryReportList(studentId);
        return responseTemplate.format(BeanUtils.convert(bos,QueryAssessmentActionFlowVO.class));
    }

    @ApiOperation(value = "student report", notes = "student report")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public Response<QueryAssessmentActionFlowVO> studentReprot(
            @ApiParam(value = "Assessment Submit VO", required = true)@RequestBody AssessmentSubmitVO assessmentSubmitVO){
        AssessmentActionFlowBO bo = assessmentActionFlowService.studentReport(assessmentSubmitVO.getProcessInstanceId(), assessmentSubmitVO.getUserId());
        return responseTemplate.format(BeanUtils.convert(bo,QueryAssessmentActionFlowVO.class));
    }
}
