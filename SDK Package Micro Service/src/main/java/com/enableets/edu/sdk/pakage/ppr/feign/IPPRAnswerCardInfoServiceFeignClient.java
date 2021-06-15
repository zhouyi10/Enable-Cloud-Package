package com.enableets.edu.sdk.pakage.ppr.feign;

import com.enableets.edu.sdk.pakage.ppr.dto.AddAnswerCardInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryAnswerCardInfoResultDTO;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/02
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRAnswerCardInfoServiceFeignClient {

    @GetMapping(value = "/microservice/packageservice/ppr/answercards/{id}")
    public Response<QueryAnswerCardInfoResultDTO> getAnswerCard(@PathVariable(value = "id") String id, @RequestParam(value = "userId", required = false) String userId);

    @PostMapping(value = "/microservice/packageservice/ppr/answercards")
    public Response<QueryAnswerCardInfoResultDTO> add(AddAnswerCardInfoDTO answerCardInfo);

    @GetMapping(value = "/microservice/packageservice/ppr/answercards")
    public Response<List<QueryAnswerCardInfoResultDTO>> query(
            @RequestParam(value = "examId", required = true) Long examId, @RequestParam(value = "creator", required = false) String creator);

}
