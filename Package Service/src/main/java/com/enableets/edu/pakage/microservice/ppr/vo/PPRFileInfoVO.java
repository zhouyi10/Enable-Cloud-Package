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
@ApiModel(value = "PPRFileInfoVO", description = "PPR File Info")
public class PPRFileInfoVO {

    @ApiModelProperty(value = "File ID", required = false)
    private String fileId;

    @ApiModelProperty(value = "File Name", required = false)
    private String fileName;

    @ApiModelProperty(value = "File Ext", required = false)
    private String fileExt;

    @ApiModelProperty(value = "File Link", required = false)
    private String url;

    @ApiModelProperty(value = "File Md5", required = false)
    private String md5;

    @ApiModelProperty(value = "File Size", required = false)
    private Long size;

    @ApiModelProperty(value = "File Size Display", required = false)
    private String sizeDisplay;

    @ApiModelProperty(value = "Order", required = false)
    private Integer fileOrder;
}
