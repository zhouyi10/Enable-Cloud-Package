package com.enableets.edu.pakage.framework.etm.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SemtenceInfoBO {

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

    private Date creatTime;

    private String updator;

    private Date updateTime;

    /**
     * Coordinate
     */
    private List<EtmRegionBO> semtenceInfoCoordinateList;

}
