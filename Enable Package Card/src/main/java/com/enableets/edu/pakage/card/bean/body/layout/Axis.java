package com.enableets.edu.pakage.card.bean.body.layout;

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
@XStreamAlias("axis")
public class Axis {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String type;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private Double xAxis;

    @XStreamAsAttribute
    private Double yAxis;

    @XStreamAsAttribute
    private Double width;

    @XStreamAsAttribute
    private Double height;

    @XStreamAsAttribute
    private Long pageNo;

    @XStreamAsAttribute
    private Long optionCount;

    @XStreamAsAttribute
    private Long rowCount;
}
