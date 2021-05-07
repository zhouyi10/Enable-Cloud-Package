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
 * @since 2020/06/29
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("question")
public class LayoutQuestion {

    @XStreamAsAttribute
    private String id;

    @XStreamImplicit
    private List<Axis> axises;

    @XStreamImplicit
    private List<TimeAxis> timeAxes;
}
