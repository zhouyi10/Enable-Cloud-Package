package com.enableets.edu.pakage.card.bean.body.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
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
@XStreamAlias("layout")
public class Layout {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String pageType;

    @XStreamAsAttribute
    private String edition;

    @XStreamAsAttribute
    private String column;

    @XStreamImplicit
    private List<LayoutQuestion> questions;

}
