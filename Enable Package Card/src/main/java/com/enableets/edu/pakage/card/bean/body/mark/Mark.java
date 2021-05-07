package com.enableets.edu.pakage.card.bean.body.mark;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
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
@XStreamAlias("mark")
public class Mark {

    @XStreamImplicit
    private List<MarkItem> items;
}
