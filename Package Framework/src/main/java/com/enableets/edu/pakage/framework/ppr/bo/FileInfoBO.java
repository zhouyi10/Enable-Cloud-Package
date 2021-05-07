package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/03
 **/
@Data
public class FileInfoBO implements java.io.Serializable{

    private String fileId;

    private String fileName;

    private String fileExt;

    private String md5;

    private String url;

    private Long size;

    private String sizeDisplay;

    private Integer fileOrder;
}
