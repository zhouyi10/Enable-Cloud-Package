package com.enableets.edu.pakage.core.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
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
@XStreamAlias("item")
public class FileItem {


    @XStreamAsAttribute
    private String format;

    @XStreamImplicit
    private List<Href> hrefs;

}
