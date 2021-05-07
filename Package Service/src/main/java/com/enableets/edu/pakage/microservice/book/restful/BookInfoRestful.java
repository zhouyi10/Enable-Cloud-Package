package com.enableets.edu.pakage.microservice.book.restful;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.module.service.controller.ServiceControllerAdapter;
import com.enableets.edu.module.service.core.Response;
import com.enableets.edu.pakage.framework.book.bo.BookInfoBO;
import com.enableets.edu.pakage.framework.book.service.BookInfoService;
import com.enableets.edu.pakage.microservice.book.vo.AddBookInfoVO;
import com.enableets.edu.pakage.microservice.book.vo.QueryBookInfoResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@Api(value = "[1]Book Info Api", tags = "Book Info Api", position = 1)
@RestController
@RequestMapping(value = "/microservice/packageservice/book")
public class BookInfoRestful extends ServiceControllerAdapter<String> {


    @Autowired()
    @Qualifier("packageBookInfo")
    private BookInfoService bookInfoService;

    @ApiOperation(value = "Add Book Info", notes = "Add Book Info")
    @PostMapping(value = "/addBookInfo")
    public Response<QueryBookInfoResultVO> addBookInfo(@ApiParam(value = "Book Info", required = true) @RequestBody AddBookInfoVO addBookInfoVO) {
        BookInfoBO bookInfo = bookInfoService.add(BeanUtils.convert(addBookInfoVO, BookInfoBO.class));
        return responseTemplate.format(BeanUtils.convert(bookInfo, QueryBookInfoResultVO.class));
    }

    @ApiOperation(value = "Parse Book Info", notes = "parse Book Info")
    @PostMapping(value = "/parseBookInfo")
    public Response<QueryBookInfoResultVO> parseBookInfo(@ApiParam(value = "Book Info", required = true) @RequestParam(value = "contentId") String contentId) {
        BookInfoBO bookInfo = bookInfoService.parse(contentId);
        return responseTemplate.format(BeanUtils.convert(bookInfo, QueryBookInfoResultVO.class));
    }
}
