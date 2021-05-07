package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
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
