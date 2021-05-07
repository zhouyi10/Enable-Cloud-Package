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
@XStreamAlias("item")
public class BodyItem {

    @XStreamAsAttribute
    private String id;

    @XStreamAsAttribute
    private String sequence;

    @XStreamAsAttribute
    private String media;

    @XStreamAsAttribute
    private String mediaSrc;

    private Content content;


    private List<Region> regionList;

}
