package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "PPRQuestionAxisVO", description = "PPR Question Axis")
public class PPRQuestionAxisVO {

    @ApiModelProperty(value = "Question ID", required = false)
    private String questionId;

    @ApiModelProperty(value = "File ID", required = false)
    private String fileId;

    @ApiModelProperty(value = "X Axis Coordinate", required = false)
    private Double xAxis;

    @ApiModelProperty(value = "Y Axis Coordinate", required = false)
    private Double yAxis;

    @ApiModelProperty(value = "Area Width", required = false)
    private Double width;

    @ApiModelProperty(value = "Area Height", required = false)
    private Double height;

    @ApiModelProperty(value = "Order", required = false)
    private Integer sequence;
}
