package com.enableets.edu.pakage.card.bean.body.mark;

import com.enableets.edu.pakage.card.bean.body.FileItem;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/19
 **/
@Data
@XStreamAlias("markResult")
public class MarkResult {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private float score;

    @XStreamAsAttribute
    private String status;

    @XStreamAsAttribute
    private String userId;

    @XStreamAsAttribute
    private String fullName;

    @XStreamAsAttribute
    private Long timestamp;

    private String comment;

    private List<FileItem> files;
}
