package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.enableets.edu.sdk.ppr.annotation.PaperProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PPR Node Info
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeInfoBO {

    public NodeInfoBO(String nodeId, String parentNodeId, String name, Integer level, QuestionInfoBO question){
        this.nodeId = nodeId;
        this.parentNodeId = parentNodeId;
        this.name = name;
        this.level = level;
        this.question = question;
    }

    @PaperProperties(value = "id")
    private String nodeId;

    private String parentNodeId;

    @PaperProperties(value = "title")
    private String name;

    private String description;

    private Integer level;

    @XStreamImplicit
    private Float points;

    private QuestionInfoBO question;

    protected String href;
}
