package com.enableets.edu.sdk.pakage.book.impl;

import com.enableets.edu.sdk.pakage.book.dto.AddBookInfoDTO;
import com.enableets.edu.sdk.pakage.book.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.pakage.book.feign.IBookInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.book.service.IBookPackageService;

public class DefaultBookInfoService implements IBookPackageService {


    private IBookInfoServiceFeignClient bookInfoServiceFeignClient;

    public DefaultBookInfoService(IBookInfoServiceFeignClient bookInfoServiceFeignClient) {
        this.bookInfoServiceFeignClient = bookInfoServiceFeignClient;
    }


    @Override
    public QueryBookInfoResultDTO addBookInfo(AddBookInfoDTO addBookInfoDTO) {
        return bookInfoServiceFeignClient.addBookInfo(addBookInfoDTO).getData();
    }

    @Override
    public QueryBookInfoResultDTO parseBookInfo(String contentId) {
        return bookInfoServiceFeignClient.parseBookInfo(contentId).getData();
    }
}
