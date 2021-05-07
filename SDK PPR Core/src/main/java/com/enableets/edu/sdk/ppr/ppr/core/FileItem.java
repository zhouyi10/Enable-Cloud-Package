package com.enableets.edu.sdk.ppr.ppr.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/17
 **/
@Data
@NoArgsConstructor
@XStreamAlias("item")
public class FileItem {

    public FileItem(String format, List<Href> hrefs){
        this.format = format;
        this.hrefs = hrefs;
    }

    public FileItem(String format, String fileId, String fileName, String md5, String url){
        this.format = format;
        this.fileId = fileId;
        this.fileName = fileName;
        this.md5 = md5;
        this.url = url;
    }

    @XStreamAsAttribute
    private String format;

    @XStreamAsAttribute
    private String fileId;

    @XStreamAsAttribute
    private String fileName;

    @XStreamAsAttribute
    private String md5;

    @XStreamAsAttribute
    private String url;

    @XStreamImplicit
    private List<Href> hrefs;
}
