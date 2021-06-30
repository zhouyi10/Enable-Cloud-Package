package com.enableets.edu.pakage.microservice.ppr.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.ppr.bo.PPRInfoBO;
import com.enableets.edu.pakage.framework.ppr.paper.service.PPRInfoService;
import com.enableets.edu.pakage.microservice.ppr.vo.AddPPRInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.EditPPRInfoVO;
import com.enableets.edu.pakage.microservice.ppr.vo.QueryPPRInfoResultVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Api(value = "(1)PPR Info Api", tags = "(1)PPR Info Api", position = 1)
@RestController
@RequestMapping(value = "/microservice/packageservice/ppr")
public class PPPInfoRestful extends ServiceControllerAdapter<String> {

    @Autowired
    private PPRInfoService pprInfoService;

    @ApiOperation(value = "Add PPR Info", notes = "Add PPR Info")
    @PostMapping(value = "")
    public Response<QueryPPRInfoResultVO> add(@ApiParam(value = "PPR Info", required = true) @RequestBody AddPPRInfoVO addPPRInfoVO){
        PPRInfoBO pprInfoBO = pprInfoService.add(BeanUtils.convert(addPPRInfoVO, PPRInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(pprInfoBO, QueryPPRInfoResultVO.class));
    }

    @ApiOperation(value = "Get PPR Info", notes = "Get PPR Info")
    @GetMapping(value = "/{id}")
    public Response<QueryPPRInfoResultVO> get(@ApiParam(value = "Primary Key", required = true) @PathVariable("id") String id){
        PPRInfoBO pprInfoBO = pprInfoService.get(id);
        return responseTemplate.format(BeanUtils.convert(pprInfoBO, QueryPPRInfoResultVO.class));
    }

    @ApiOperation(value = "Edit PPR Info", notes = "Edit PPR Info")
    @PutMapping(value = "/{id}")
    public Response<QueryPPRInfoResultVO> edit(@ApiParam(value = "Primary Key", required = true) @PathVariable("id") String id, @ApiParam(value = "PPR Info", required = true) @RequestBody EditPPRInfoVO editPPRInfoVO){
        PPRInfoBO pprInfo = BeanUtils.convert(editPPRInfoVO, PPRInfoBO.class);
        pprInfo.setPaperId(Long.valueOf(id));
        PPRInfoBO pprInfoBO = pprInfoService.edit(pprInfo);
        return responseTemplate.format(BeanUtils.convert(pprInfoBO, QueryPPRInfoResultVO.class));
    }
}
