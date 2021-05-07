package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.enableets.edu.sdk.ppr.ppr.bo.CodeNameMapBO;
import com.enableets.edu.sdk.ppr.ppr.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PPRBO {

    /** Paper ID */
    private Long paperId;

    /** Paper Name */
    private String name;

    /** Stage Info */
    private CodeNameMapBO stage;

    /** Grade Info */
    private CodeNameMapBO grade;

    /** Subject Info */
    private CodeNameMapBO subject;

    /** Paper Score */
    private Float totalPoints;

    /** Recommended answer time  */
    private Long answerCostTime;

    /** Textbook version  */
    private IdNameMapBO materialVersion;

    /** School Info */
    private IdNameMapBO school;

    /** User Info */
    private IdNameMapBO user;

    /** Resource ID */
    private Long contentId;

    /** Create Time*/
    private Date createTime;

    /** Test paper node information */
    private List<NodeInfoBO> nodes;

    /***/
    private List<KnowledgeInfoBO> knowledges;

    /***/
    private List<FileInfoBO> files;
}
