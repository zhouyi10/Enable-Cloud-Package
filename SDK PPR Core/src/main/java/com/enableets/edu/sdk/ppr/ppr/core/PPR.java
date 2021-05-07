package com.enableets.edu.sdk.ppr.ppr.core;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
@Data
public class PPR {

    private String fileId;

    private String name;

    private String downloadUrl;

    private String md5;

    private Long size;

    private String sizeDisplay;

    private String ext;
}
