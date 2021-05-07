package com.enableets.edu.pakage.card.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/19
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("item")
public class FileItem {

    @XStreamAsAttribute
    private String format;

    @XStreamAsAttribute
    private String fileId;

    @XStreamAsAttribute
    private String fileName;

    @XStreamAsAttribute
    private String url;

    private String href;
}
