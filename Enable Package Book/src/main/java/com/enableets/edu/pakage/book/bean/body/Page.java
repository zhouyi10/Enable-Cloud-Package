package com.enableets.edu.pakage.book.bean.body;

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
    private String sequence;

    @XStreamAsAttribute
    private String image;

    @XStreamAsAttribute
    private String url;

    @XStreamAsAttribute
    private String width;

    @XStreamAsAttribute
    private String height;

   /* @XStreamAsAttribute
    private String affixPath;*/

    @XStreamAsAttribute
    private String affixUrl;

    @XStreamImplicit
    private List<BodyItem> bodyItems;

}
