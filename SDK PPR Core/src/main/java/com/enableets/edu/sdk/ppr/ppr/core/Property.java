package com.enableets.edu.sdk.ppr.ppr.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/16 14:00
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("property")
public class Property {

    @XStreamImplicit
    private List<Item> items;

}
