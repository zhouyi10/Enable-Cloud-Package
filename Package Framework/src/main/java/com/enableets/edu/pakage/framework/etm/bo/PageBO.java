package com.enableets.edu.pakage.framework.etm.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PageBO {

    private String pageInfoId;

    private String sequence;

    private String etmBookId;

    private Long contentId;

    private String pageInfoImgWidth;

    private String pageInfoImgHeight;

    private String pageInfoImgId;

    private String pageInfoImgName;

    private String pageInfoImgRealUrl;

    private String pageInfoMp3Id;

    private String pageInfoMp3Name;

    private String pageInfoMp3LoadUrl;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<SemtenceInfoBO> semtenceInfoList;

}
