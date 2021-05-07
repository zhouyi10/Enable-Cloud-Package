package com.enableets.edu.pakage.manager.etm.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PageVO {

    private String pageInfoId;

    private String sequence;

    private String etmBookId;

    private Long contentId;

    private String pageInfoImgId;

    private String pageInfoImgWidth;

    private String pageInfoImgHeight;

    private String pageInfoImgName;

    private String pageInfoImgRealUrl;

    private String pageInfoMp3Id;

    private String pageInfoMp3Name;

    private String pageInfoMp3LoadUrl;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<SemtenceInfoVO> semtenceInfoList;

}
