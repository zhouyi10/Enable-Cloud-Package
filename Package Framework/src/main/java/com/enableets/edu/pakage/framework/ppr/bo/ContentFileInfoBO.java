package com.enableets.edu.pakage.framework.ppr.bo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import lombok.Data;

/**
 * Resource File Entity
 * @author walle_yu@enable-ets.com
 * @since 2020/09/27
 **/
@Data
public class ContentFileInfoBO {

    private String contentId;
    private String fileId;
    private String fileName;
    private String fileExt;
    private String url;
    private String md5;
    private Long size;
    private String sizeDisplay;
    private String description;
    private Integer fileOrder;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date uploadDate;
    private String providerCode;
    private Integer downloadNumber;
    private String extendAttrs;
}
