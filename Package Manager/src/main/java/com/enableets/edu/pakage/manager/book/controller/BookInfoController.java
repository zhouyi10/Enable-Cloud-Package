package com.enableets.edu.pakage.manager.book.controller;

import com.enableets.edu.pakage.manager.book.bo.BookInfoBO;
import com.enableets.edu.pakage.manager.book.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/manager/package/book")
public class BookInfoController {

    @Autowired
    private BookInfoService bookInfoService;

    @RequestMapping(value = "/addBookInfo", method = RequestMethod.GET)
    @ResponseBody
    public BookInfoBO addBookInfo(@RequestParam(value = "bookId") String bookId) {
        BookInfoBO bookInfoBO = bookInfoService.addBookInfo(bookId);
        return bookInfoBO;
    }

    @RequestMapping(value = "/parseBookInfo", method = RequestMethod.GET)
    @ResponseBody
    public BookInfoBO parseBookInfo(@RequestParam(value = "contentId") String contentId) {
        BookInfoBO bookInfoBO = bookInfoService.parseBookInfo(contentId);
        return bookInfoBO;
    }
}
