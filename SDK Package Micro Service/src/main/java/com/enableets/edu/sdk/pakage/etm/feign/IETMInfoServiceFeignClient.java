package com.enableets.edu.sdk.pakage.etm.feign;


import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.AddETMPageDTO;
import com.enableets.edu.sdk.pakage.etm.dto.EditETMInfoDTO;
import com.enableets.edu.sdk.pakage.etm.dto.QueryETMInfoResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IETMInfoServiceFeignClient {

    @PostMapping(value = "/microservice/packageservice/etm/addETMInfo")
    public Response<QueryETMInfoResultDTO> addETMInfo(@RequestBody AddETMInfoDTO addETMInfoDTO);

    @PostMapping(value = "/microservice/packageservice/etm/parseETMInfo")
    public Response<QueryETMInfoResultDTO> parseETMInfo(@RequestParam(value = "contentId")Long contentId);

    @PostMapping(value = "/microservice/packageservice/etm/editETMInfo")
    public Response<QueryETMInfoResultDTO> editETMInfo(EditETMInfoDTO editETMInfoDTO);

    @PostMapping(value = "/microservice/packageservice/etm/editOnePage")
    public Response<QueryETMInfoResultDTO> editOnePage(AddETMPageDTO addETMPageDTO);

    @PostMapping(value = "/microservice/packageservice/etm/deleteOnePage")
    public Response<Boolean> deleteOnePage(String pageInfoId);

    @PostMapping(value = "/microservice/packageservice/etm/queryETMInfo")
    public Response<QueryETMInfoResultDTO> queryETMInfo(@RequestParam(value = "bookId")String bookId);
}
