package com.enableets.edu.pakage.ppr.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("section")
public class Section {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String title;

    @XStreamAsAttribute
    private Float score;

    @XStreamImplicit
    private List<Question> questions;
}
