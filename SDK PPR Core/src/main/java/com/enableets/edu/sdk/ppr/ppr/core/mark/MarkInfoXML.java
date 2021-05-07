package com.enableets.edu.sdk.ppr.ppr.core.mark;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/03
 **/
@XStreamAlias("paperCard")
public class MarkInfoXML {

    @XStreamImplicit
    private List<MarkItem> items;
}
