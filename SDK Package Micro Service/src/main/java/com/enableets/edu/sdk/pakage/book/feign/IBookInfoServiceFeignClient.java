package com.enableets.edu.sdk.pakage.book.feign;


import com.enableets.edu.sdk.core.Response;
import com.enableets.edu.sdk.pakage.book.dto.AddBookInfoDTO;
import com.enableets.edu.sdk.pakage.book.dto.QueryBookInfoResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${sdk.package-microservice.name:package-microservice}")
public interface IBookInfoServiceFeignClient {

    @PostMapping(value = "/microservice/packageservice/book/addBookInfo")
    public Response<QueryBookInfoResultDTO> addBookInfo(@RequestBody AddBookInfoDTO addBookInfoDTO);

    @PostMapping(value = "/microservice/packageservice/book/parseBookInfo")
    public Response<QueryBookInfoResultDTO> parseBookInfo(@RequestParam(value = "contentId") String contentId);

}
