package com.enableets.edu.pakage.manager.etm.vo;


import lombok.Data;

import java.util.Date;

@Data
public class RegionVO {

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

    private Date creatTime;

    private String updator;

    private Date updateTime;

}
