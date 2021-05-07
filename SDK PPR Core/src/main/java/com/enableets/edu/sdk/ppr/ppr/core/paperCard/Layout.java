package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("layout")
public class Layout {

    public Layout(String pageType) {
        this.pageType = pageType;
    }

    @XStreamAsAttribute
    private String pageType;

    @XStreamImplicit
    private List<LayoutQuestion> questions;
}
