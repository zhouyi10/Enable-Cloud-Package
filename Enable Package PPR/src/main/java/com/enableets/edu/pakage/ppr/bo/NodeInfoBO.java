package com.enableets.edu.pakage.ppr.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * PPR structure Node
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeInfoBO {

    private String nodeId;

    private String parentNodeId;

    private String name;

    private String description;

    private Integer level;

    private Integer internalNo;

    private String externalNo;

    private Float points;

    private QuestionInfoBO question;
}
