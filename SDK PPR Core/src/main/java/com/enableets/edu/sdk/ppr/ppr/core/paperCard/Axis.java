package com.enableets.edu.sdk.ppr.ppr.core.paperCard;

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
    private Double xAxis;

    @XStreamAsAttribute
    private Double yAxis;

    @XStreamAsAttribute
    private Double width;

    @XStreamAsAttribute
    private Double height;

    @XStreamAsAttribute
    private Long pageNo;
}
