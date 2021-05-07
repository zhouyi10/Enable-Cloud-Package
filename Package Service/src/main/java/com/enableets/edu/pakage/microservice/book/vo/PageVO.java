package com.enableets.edu.pakage.microservice.book.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PageVO {

    private String pageId;

    private String sequence;

    private String name;

    private String bookId;

    private Long contentId;

    private String width;

    private String height;

    private String affixUrl;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private FileInfoVO pageFile;

    private List<SemtenceInfoVO> semtenceInfoList;

}
