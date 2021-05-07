package com.enableets.edu.sdk.pakage.etm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class SemtenceInfoDTO {

    /**
     * semtenceId
     */
    private String semtenceId;

    private String sequence;

    private String pageInfoId;

    /**
     * mp3 id
     */
    private String semtenceInfoMp3Id;

    /**
     * mp3 name
     */
    private String semtenceInfoMp3Name;

    /**
     * mp3 url
     */
    private String semtenceInfoMp3LoadUrl;

    /**
     * editor content
     */
    private String semtenceInfoText;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * Coordinate
     */
    private List<RegiondDTO> semtenceInfoCoordinateList;


}
