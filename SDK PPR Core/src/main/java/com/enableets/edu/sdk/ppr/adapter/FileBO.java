package com.enableets.edu.sdk.ppr.adapter;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
@Data
public class FileBO {

    private String fileId;

    private String downloadUrl;

    private String md5;

    private String name;

    private Long size;

    private String sizeDisplay;

    private String ext;
}
