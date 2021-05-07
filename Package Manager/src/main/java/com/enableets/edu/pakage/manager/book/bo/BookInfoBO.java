package com.enableets.edu.pakage.manager.book.bo;


import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookInfoBO {

    private  String bookId;

    private  String bookName;

    private  String isbn;

    private CodeNameMapBO stage;

    private CodeNameMapBO grade;

    private CodeNameMapBO subject;

    private CodeNameMapBO textBookVersion;

    private CodeNameMapBO term;

    private IdNameMapBO user;

    private FileInfoBO cover;

    private Long contentId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageBO> pageList;


}
