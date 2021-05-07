package com.enableets.edu.pakage.etm.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("page")
public class Page {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String sequence;

    @XStreamAsAttribute
    private String file;

    @XStreamAsAttribute
    private String imgSrc;

    @XStreamAsAttribute
    private String width;

    @XStreamAsAttribute
    private String height;

    @XStreamAsAttribute
    private String media;

    @XStreamAsAttribute
    private String mediaSrc;

    @XStreamImplicit
    private List<BodyItem> bodyItems;

}
