package com.enableets.edu.sdk.pakage.book.dto;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SemtenceInfoDTO {

    /** question id */
    private String questionId;

    /** question parent id*/
    private String parentId;

    /**
     * sequenceId
     */
    private String sequenceId;

    /**
     *  affix url
     */
    private String affixUrl;

    /**
     * question content
     */
    private String text;

    /** page Id */
    private String pageId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    /** content region*/
    private ContentRegionDTO contentRegionDTO;

    /**
     * Coordinate
     */
    private List<RegiondDTO> bookRegionInfoList;


}
