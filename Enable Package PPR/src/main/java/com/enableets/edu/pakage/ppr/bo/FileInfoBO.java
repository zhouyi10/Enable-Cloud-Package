package com.enableets.edu.pakage.ppr.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * File Info
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfoBO {

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;

    private String md5;

    private Long size;

    private String sizeDisplay;

    private Integer fileOrder;
}
