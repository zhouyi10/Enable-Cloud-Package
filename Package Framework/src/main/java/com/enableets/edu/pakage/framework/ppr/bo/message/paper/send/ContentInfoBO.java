package com.enableets.edu.pakage.framework.ppr.bo.message.paper.send;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/17
 **/
@Data
@Accessors(chain = true)
public class ContentInfoBO {

    private String contentId;

    private String description;

    private String extendAttrs;

    private String name;

    private Integer sequence;

    private String typeCode;

    private String typeName;

    private List<ContentFileInfo> files;

    @Data
    @Accessors(chain = true)
    public static class ContentFileInfo{
        private String contentId;

        private String fileId;

        private String md5;

        private Long size;

        private String sizeDisplay;

        private String businessId;

        private String type;

        private String Description;

        private List<UrlInfo> urls;

        @Data
        public static class UrlInfo{
            private String url;
        }
    }
}
