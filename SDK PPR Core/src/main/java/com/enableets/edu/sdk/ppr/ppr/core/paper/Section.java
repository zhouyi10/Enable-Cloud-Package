package com.enableets.edu.sdk.ppr.ppr.core.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("section")
public class Section {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String title;

    @XStreamImplicit
    private List<Question> questions;
}
