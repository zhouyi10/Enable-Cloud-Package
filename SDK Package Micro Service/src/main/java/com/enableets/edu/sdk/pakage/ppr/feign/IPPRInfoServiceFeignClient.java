package com.enableets.edu.sdk.pakage.ppr.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.ppr.dto.AddPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.EditPPRInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPPRInfoResultDTO;

/**
 * PPR Info Client
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRInfoServiceFeignClient {

    @PostMapping(value = "/microservice/packageservice/ppr")
    public Response<QueryPPRInfoResultDTO> add(@RequestBody AddPPRInfoDTO addPPRInfoDTO);

    @GetMapping(value = "/microservice/packageservice/ppr/{id}")
    public Response<QueryPPRInfoResultDTO> get(@PathVariable("id") String id);

    @PutMapping(value = "/microservice/packageservice/ppr/{id}")
    public Response<QueryPPRInfoResultDTO> edit(@PathVariable("id") String id, @RequestBody EditPPRInfoDTO editPPRInfoDTO);
}
