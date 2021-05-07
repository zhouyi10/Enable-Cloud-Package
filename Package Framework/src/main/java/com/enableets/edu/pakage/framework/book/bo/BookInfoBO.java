package com.enableets.edu.pakage.framework.book.bo;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BookInfoBO {

    private  Long bookId;

    private  String bookName;

    private  String isbn;

    private BookCodeNameMapBO stage;

    private BookCodeNameMapBO grade;

    private BookCodeNameMapBO subject;

    private BookCodeNameMapBO textBookVersion;

    private BookCodeNameMapBO term;

    private BookIdNameMapBO user;

    private BookFileInfoBO cover;

    private Long contentId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageBO> pageList;


}
