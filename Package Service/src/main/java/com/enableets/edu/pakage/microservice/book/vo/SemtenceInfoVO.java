package com.enableets.edu.pakage.microservice.book.vo;


import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SemtenceInfoVO {

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
    private ContentRegionVO contentRegionVO;

    /** Coordinate */
    private List<RegionVO> bookRegionInfoList;



}
