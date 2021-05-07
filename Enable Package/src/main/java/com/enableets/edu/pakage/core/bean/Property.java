package com.enableets.edu.pakage.core.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("property")
public class Property {

    @XStreamImplicit
    private List<Item> items;

}
