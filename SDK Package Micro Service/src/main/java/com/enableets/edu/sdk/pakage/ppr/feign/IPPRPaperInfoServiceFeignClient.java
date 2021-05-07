package com.enableets.edu.sdk.pakage.ppr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.ppr.dto.AddPaperInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPaperInfoResultDTO;

/**
 * Package-ppr Paper Client
 * @author walle_yu@enable-ets.com
 * @since 2020/09/09
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRPaperInfoServiceFeignClient {

    @RequestMapping(value = "/microservice/packageservice/ppr/paper/{paperId}", method = RequestMethod.GET)
    public Response<QueryPaperInfoResultDTO> get(@PathVariable("paperId") Long paperId);

    @RequestMapping(value = "/microservice/packageservice/ppr/paper", method = RequestMethod.POST)
    public Response<QueryPaperInfoResultDTO> add(@RequestBody AddPaperInfoDTO paperInfoDTO);
}
