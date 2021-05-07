package com.enableets.edu.sdk.pakage.etm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class RegiondDTO {

    private String regionId;

    private String semtenceId;

    private String x;

    private String y;

    private String width;

    private String height;

    /**
     * div identification
     */
    private String semtenceInfoDiv;

    /**
     * top location
     */
    private String semtenceYtop;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
