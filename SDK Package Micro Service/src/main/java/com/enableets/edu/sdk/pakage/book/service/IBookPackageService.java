package com.enableets.edu.sdk.pakage.book.service;

import com.enableets.edu.sdk.pakage.book.dto.AddBookInfoDTO;
import com.enableets.edu.sdk.pakage.book.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.pakage.etm.dto.QueryETMInfoResultDTO;


public interface IBookPackageService {

    public QueryBookInfoResultDTO addBookInfo(AddBookInfoDTO addBookInfoDTO);

    public QueryBookInfoResultDTO parseBookInfo(String contentId);
}
