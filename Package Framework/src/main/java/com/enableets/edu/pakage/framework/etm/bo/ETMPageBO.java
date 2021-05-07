package com.enableets.edu.pakage.framework.etm.bo;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ETMPageBO {

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

    private EtmCodeNameMapBO stage;

    private EtmCodeNameMapBO grade;

    private EtmCodeNameMapBO subject;

    private EtmCodeNameMapBO textBookVersion;

    private EtmCodeNameMapBO term;

    private EtmIdNameMapBO user;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private String providerCode;

    private Long contentId;

    private String type;

    private String layoutType;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private PageBO page;


}
