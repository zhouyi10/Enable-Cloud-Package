package com.enableets.edu.pakage.card.bean.body.layout;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * create by daniel_yin@enable-ets.com
 * at 2020/12/28 15:55
 * description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("timeline")
public class TimeAxis {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String type;

    @XStreamAsAttribute
    private String name;

    @XStreamAsAttribute
    private Integer triggerTime;

    @XStreamAsAttribute
    private Long pageNo;

    @XStreamAsAttribute
    private Long optionCount;

}
