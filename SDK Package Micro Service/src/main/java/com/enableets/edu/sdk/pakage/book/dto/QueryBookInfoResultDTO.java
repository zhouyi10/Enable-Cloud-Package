package com.enableets.edu.sdk.pakage.book.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryBookInfoResultDTO {


    private  Long bookId;

    private  String bookName;

    private  String isbn;

    private CodeNameMapDTO stage;

    private CodeNameMapDTO grade;

    private CodeNameMapDTO subject;

    private CodeNameMapDTO textBookVersion;

    private CodeNameMapDTO term;

    private IdNameMapDTO user;

    private FileInfoDTO cover;

    private Long contentId;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<PageDTO> pageList;
}
