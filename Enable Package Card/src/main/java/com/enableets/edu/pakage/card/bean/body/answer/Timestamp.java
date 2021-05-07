package com.enableets.edu.pakage.card.bean.body.answer;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("timestamp")
public class Timestamp {

    @XStreamAsAttribute
    private Long start;

    @XStreamAsAttribute
    private Long end;
}
