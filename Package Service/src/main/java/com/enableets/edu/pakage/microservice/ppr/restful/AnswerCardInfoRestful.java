package com.enableets.edu.pakage.microservice.ppr.restful;

import com.enableets.edu.pakage.microservice.ppr.vo.AddAnswerCardInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerCardInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryAnswerCardInfoResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
@Api(value = "(2)PPR AnswerCard Info Api", tags = "(2)PPR AnswerCard Info Api", position = 2)
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr/answercards")
public class AnswerCardInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private AnswerCardInfoService answerCardInfoService;

    /**
     * Get AnswerCard of PPR
     * @param id Paper ID
     * @param userId User ID
     * @return
     */
    @ApiOperation(value = "Get PPR AnswerCard", notes = "Get PPR AnswerCard")
    @GetMapping(value = "/{id}")
    public Response<QueryAnswerCardInfoResultVO> getAnswerCard(@ApiParam(value = "PPR Primary key", required = true) @PathVariable("id") String id, @ApiParam(value = "Card Creator", required = false) @RequestParam(value = "userId", required = false) String userId){
        AnswerCardInfoBO answerCard = answerCardInfoService.getAnswerCard(id, userId);
        return responseTemplate.format(BeanUtils.convert(answerCard, QueryAnswerCardInfoResultVO.class));
    }

    @ApiOperation(value = "Add Answer Card", notes = "Add Answer Card Of Paper")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Response<QueryAnswerCardInfoResultVO> add(@ApiParam(value = "Answer Card Info", required = true) @RequestBody AddAnswerCardInfoVO addAnswerCardInfoVO){
        AnswerCardInfoBO answerCard = answerCardInfoService.add(BeanUtils.convert(addAnswerCardInfoVO, AnswerCardInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(answerCard, QueryAnswerCardInfoResultVO.class));
    }

    @ApiOperation(value ="Query Answer Card", notes ="I_49_03_005")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Response<List<QueryAnswerCardInfoResultVO>> query(
            @ApiParam(value ="examId", required = true) @RequestParam(value = "examId", required = true) Long examId,
            @ApiParam(value ="creatorId", required = false) @RequestParam(value = "creator", required = false) String creator){
        List<AnswerCardInfoBO> result = answerCardInfoService.query(examId, creator);
        return responseTemplate.format(BeanUtils.convert(result, QueryAnswerCardInfoResultVO.class));
    }
}
