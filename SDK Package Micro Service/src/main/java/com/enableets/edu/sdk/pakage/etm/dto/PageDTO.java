package com.enableets.edu.sdk.pakage.etm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class PageDTO {

    private String pageInfoId;

    private String sequence;

    private String etmBookId;

    private Long contentId;

    private String pageInfoImgId;

    private String pageInfoImgWidth;

    private String pageInfoImgHeight;

    /**
     * img name
     */
    private String pageInfoImgName;

    private String pageInfoImgRealUrl;

    private String pageInfoMp3Id;

    /**
     * mp3 name
     */
    private String pageInfoMp3Name;

    private String pageInfoMp3LoadUrl;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<SemtenceInfoDTO> semtenceInfoList;

}
