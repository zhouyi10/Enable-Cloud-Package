package com.enableets.edu.pakage.card.bean.body.action;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.Property;
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
public class ActionItem {

    public ActionItem(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String type;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private String description;

    private Property property;

    public String readProperty(String key){
        if (property != null && CollectionUtils.isNotEmpty(property.getItems())){
            for (Item item : property.getItems()) {
                if (item.getKey().equals(key)) return item.getValue();
            }
        }
        return null;
    }

}
