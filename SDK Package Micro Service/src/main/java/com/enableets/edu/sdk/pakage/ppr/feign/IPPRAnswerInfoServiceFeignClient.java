package com.enableets.edu.sdk.pakage.ppr.feign;

import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.ppr.dto.AnswerCardSubmitInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/19
 **/
@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IPPRAnswerInfoServiceFeignClient {

    @PostMapping(value = "/microservice/packageservice/ppr/answer/submit")
    public Response<String> submit(@RequestBody AnswerCardSubmitInfoDTO answerCardSubmitInfoDTO);
}
