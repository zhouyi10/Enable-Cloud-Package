package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileInfoBO {

    public FileInfoBO(){}

    public FileInfoBO(String url){
        this.url = url;
    }

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;
}
