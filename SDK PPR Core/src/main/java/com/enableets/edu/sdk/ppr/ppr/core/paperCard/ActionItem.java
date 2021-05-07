package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("item")
public class ActionItem {

    public ActionItem(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String description;

    private Property property;
}
