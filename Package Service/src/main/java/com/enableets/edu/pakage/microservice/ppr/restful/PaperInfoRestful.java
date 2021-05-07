package com.enableets.edu.pakage.microservice.ppr.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerCardInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.paper.service.PaperInfoService;
import com.enableets.edu.pakage.microservice.ppr.vo.AddAnswerCardInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.AddPaperInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryAnswerCardInfoResultVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryPaperInfoResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Paper Info Restful
 *
 * @author caleb_liu@enable-ets.com
 * @date 2020/07/21
 */

@Api(value = "PPR Paper Info Api", tags = "Paper Info Api")
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr/paper")
public class PaperInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private PaperInfoService paperInfoService;

    @Autowired
    private AnswerCardInfoService answerCardInfoService;

    @ApiOperation(value = "Get test ppr information", notes = "Get test paper information by ID")
    @RequestMapping(value = "/{paperId}", method = RequestMethod.GET)
    public Response<QueryPaperInfoResultVO> get(@ApiParam(value = "paperId", required = true) @PathVariable("paperId") String paperId){
        PaperInfoBO paperInfoBO = paperInfoService.get(paperId);
        return responseTemplate.format(BeanUtils.convert(paperInfoBO, QueryPaperInfoResultVO.class));
    }

    @ApiOperation(value = "Add ppr Information", notes = "Add ppr Information")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Response<QueryPaperInfoResultVO> add(@ApiParam(value = "Paper Info", required = true) @RequestBody AddPaperInfoVO paperInfoVO){
        PaperInfoBO paperInfoBO = paperInfoService.add(BeanUtils.convert(paperInfoVO, PaperInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(paperInfoBO, QueryPaperInfoResultVO.class));
    }

    @ApiOperation(value = "Add Answer Card", notes = "Add Answer Card Of Paper")
    @RequestMapping(value = "/answercards", method = RequestMethod.POST)
    public Response<QueryAnswerCardInfoResultVO> add(@ApiParam(value = "Answer Card Info", required = true) @RequestBody AddAnswerCardInfoVO addAnswerCardInfoVO){
        AnswerCardInfoBO answerCard = answerCardInfoService.add(BeanUtils.convert(addAnswerCardInfoVO, AnswerCardInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(answerCard, QueryAnswerCardInfoResultVO.class));
    }

}
