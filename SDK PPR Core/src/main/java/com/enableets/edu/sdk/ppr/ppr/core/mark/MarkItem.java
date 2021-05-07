package com.enableets.edu.sdk.ppr.ppr.core.mark;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/03
 **/
@Data
@XStreamAlias("markItem")
public class MarkItem {

    @XStreamAsAttribute
    private String id;

    private MarkResult markResult;
}
