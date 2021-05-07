package com.enableets.edu.pakage.core.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/16 14:49
 */

@Data
@XStreamAlias("item")
@NoArgsConstructor
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"value"})
public class Item {


    public Item(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @XStreamAsAttribute
    private String key;

    private String value;
}
