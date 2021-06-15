package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: gary_zhang@enable-ets.com
 * @Date: 2021/1/15 0:16
 * @Description: Answer card timeline information
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel(value = "CardTimeAxisVO", description = "Timeline Info About Answer Card")
public class CardTimeAxisVO extends BaseVO {

    /**
     * Answer trigger time
     */
    @ApiModelProperty(value = "Trigger Time", required = true)
    private Integer triggerTime;

    /**
     * question node ID
     */
    @ApiModelProperty(value = "Question Node ID", required = true)
    private String nodeId;

    /**
     * question id
     */
    @ApiModelProperty(value = "Question ID", required = true)
    private String questionId;

    /**
     * question parent node ID
     */
    @ApiModelProperty(value = "Parent Node ID", required = true)
    private String parentNodeId;

    /**
     * parent id
     */
    @ApiModelProperty(value = "Parent Question ID", required = true)
    private String parentId;

    /**
     * sequencing
     */
    @ApiModelProperty(value = "The order of blank lines about a question", required = true)
    private Long sequencing;

    /**
     * page no
     */
    @ApiModelProperty(value = "The Page No. Of Answer Card", required = true)
    private Long pageNo;

    /**
     * Question type code
     */
    @ApiModelProperty(value = "Question Type", required = true)
    private String typeCode;

    /**
     * Question type name
     */
    @ApiModelProperty(value = "Question Type Name")
    private String typeName;

    /**
     * Number of options (default: 4)
     */
    @ApiModelProperty(value = "The Number of Question Option(Default：4)")
    private Long optionCount;

    /**
     * Number of lines
     */
    @ApiModelProperty(value = "Number of lines About Answer area")
    private Long rowCount;

    @Override
    public void validate() throws MicroServiceException {
        notNull(triggerTime, "triggerTime");
        notBlank(nodeId, "nodeId");
        notBlank(questionId, "questionId");
        notBlank(parentNodeId, "parentNodeId");
        notBlank(parentId, "parentId");
        notNull(sequencing, "sequencing");
        notNull(pageNo, "pageNo");
        notBlank(typeCode, "typeCode");
    }
}
