package com.enableets.edu.pakage.framework.book.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SemtenceInfoBO {

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

    /** question content region*/
    private BookContentRegionBO contentRegionBO;

    /**
     * Coordinate
     */
    private List<BookRegionBO> bookRegionInfoList;

}
