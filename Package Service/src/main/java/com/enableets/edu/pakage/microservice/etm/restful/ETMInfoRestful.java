package com.enableets.edu.pakage.microservice.etm.restful;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.etm.bo.ETMInfoBO;
import com.enableets.edu.pakage.framework.etm.bo.ETMPageBO;
import com.enableets.edu.pakage.framework.etm.service.ETMInfoService;
import com.enableets.edu.pakage.microservice.etm.vo.AddETMInfoVO;
import com.enableets.edu.pakage.microservice.etm.vo.AddETMPageVO;
import com.enableets.edu.pakage.microservice.etm.vo.EditETMInfoVO;
import com.enableets.edu.pakage.microservice.etm.vo.QueryETMInfoResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "[1]ETM Info Api", tags = "ETM Info Api", position = 1)
@RestController
@RequestMapping(value = "/microservice/packageservice/etm")
public class ETMInfoRestful extends ServiceControllerAdapter<String> {


    @Autowired
    private ETMInfoService etmInfoService;

    @ApiOperation(value = "Add ETM Info", notes = "Add ETM Info")
    @PostMapping(value = "/addETMInfo")
    public Response<QueryETMInfoResultVO> addETMInfo(@ApiParam(value = "ETM Info", required = true) @RequestBody AddETMInfoVO addETMInfoVO) {
        ETMInfoBO etmInfoBO = etmInfoService.add(BeanUtils.convert(addETMInfoVO, ETMInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(etmInfoBO, QueryETMInfoResultVO.class));
    }

    @ApiOperation(value = "Parse ETM Info", notes = "parse ETM Info")
    @PostMapping(value = "/parseETMInfo")
    public Response<QueryETMInfoResultVO> parseETMInfo(@ApiParam(value = "ETM Info", required = true)@RequestParam(value = "contentId")Long contentId) {
        ETMInfoBO etmInfoBO = etmInfoService.parse(contentId);
        return responseTemplate.format(BeanUtils.convert(etmInfoBO, QueryETMInfoResultVO.class));
    }

    @ApiOperation(value = "Edit ETM Info", notes = "edit ETM Info")
    @PostMapping(value = "/editETMInfo")
    public Response<QueryETMInfoResultVO> editETMInfo(@ApiParam(value = "ETM Info", required = true) @RequestBody EditETMInfoVO editETMInfoVO) {
        ETMInfoBO etmInfoBO = etmInfoService.edit(BeanUtils.convert(editETMInfoVO, ETMInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(etmInfoBO, QueryETMInfoResultVO.class));
    }

    @ApiOperation(value = "Edit Etm Page", notes = "edit One Page in Etm")
    @PostMapping(value = "/editOnePage")
    public Response<QueryETMInfoResultVO> editOnePage(@ApiParam(value = "ETM Info", required = true) @RequestBody AddETMPageVO addETMPageVO) {
        ETMInfoBO etmInfoBO = etmInfoService.editOnePage(BeanUtils.convert(addETMPageVO, ETMPageBO.class));
        return responseTemplate.format(BeanUtils.convert(etmInfoBO, QueryETMInfoResultVO.class));
    }

    @ApiOperation(value = "Delete Etm Page", notes = "delete One Page in Etm")
    @PostMapping(value = "/deleteOnePage")
    public Response<Boolean> deleteOnePage(@ApiParam(value = "Page ID", required = true) @RequestBody String pageInfoId) {
        etmInfoService.deleteOnePage(pageInfoId);
        return responseTemplate.format(Boolean.TRUE);
    }

    @ApiOperation(value = "Query ETM Info", notes = "query ETM Info")
    @PostMapping(value = "/queryETMInfo")
    public Response<QueryETMInfoResultVO> queryETMInfo(@ApiParam(value = "ETM Info", required = true)@RequestParam(value = "bookId")String bookId) {
        ETMInfoBO etmInfoBO = etmInfoService.query(bookId);
        return responseTemplate.format(BeanUtils.convert(etmInfoBO, QueryETMInfoResultVO.class));
    }
}
