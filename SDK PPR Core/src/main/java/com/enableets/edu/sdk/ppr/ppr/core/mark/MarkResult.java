package com.enableets.edu.sdk.ppr.ppr.core.mark;

import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/03
 **/
@Data
@XStreamAlias("markResult")
public class MarkResult {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private Float score;

    @XStreamAsAttribute
    private String status;

    @XStreamAsAttribute
    private String userId;

    @XStreamAsAttribute
    private String fullname;

    @XStreamAsAttribute
    private Long timestamp;

    private String comment;

    private List<FileItem> files;
}
