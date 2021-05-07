package com.enableets.edu.pakage.microservice.book.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "QueryETMInfoResultVO", description = "Query ETM Info")
public class QueryBookInfoResultVO {

    private Long bookId;

    private String bookName;

    private String isbn;

    private CodeNameMapVO stage;

    private CodeNameMapVO grade;

    private CodeNameMapVO subject;

    private CodeNameMapVO textBookVersion;

    private CodeNameMapVO term;

    private IdNameMapVO user;

    private FileInfoVO cover;

    private String contentId;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<PageVO> pageList;


}
