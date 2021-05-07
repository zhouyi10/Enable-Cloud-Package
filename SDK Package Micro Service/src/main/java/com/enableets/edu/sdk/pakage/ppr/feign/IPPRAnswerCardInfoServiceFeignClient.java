package com.enableets.edu.sdk.pakage.ppr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRAnswerCardInfoServiceFeignClient {

    @GetMapping(value = "/microservice/packageservice/ppr/answercards/{id}")
    public Response<QueryAnswerCardInfoResultDTO> getAnswerCard(@PathVariable(value = "id") String id, @RequestParam(value = "userId", required = false) String userId);

}
