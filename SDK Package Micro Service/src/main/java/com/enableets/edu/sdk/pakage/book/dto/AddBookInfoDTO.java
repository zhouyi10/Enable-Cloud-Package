package com.enableets.edu.sdk.pakage.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddBookInfoDTO {

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

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageDTO> pageList;
}
