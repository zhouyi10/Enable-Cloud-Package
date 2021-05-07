package com.enableets.edu.pakage.framework.book.bo;


import lombok.Data;

import java.util.Date;

@Data
public class BookRegionBO {

    private String regionId;

    private String questionId;

    private String parentId;

    private String sequence;

    private String x;

    private String y;

    private String width;

    private String height;

    private String text;

    private String affixUrl;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;
}
