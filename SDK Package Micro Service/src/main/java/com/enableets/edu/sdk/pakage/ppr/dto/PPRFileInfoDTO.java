package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PPRFileInfoDTO {

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;

    private String md5;

    private Long size;

    private String sizeDisplay;

    private Integer fileOrder;
}
