package com.enableets.edu.pakage.book.bean.body;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
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
    private String sequence;

   /* @XStreamAsAttribute
    private String affixPath;*/

    @XStreamAsAttribute
    private String affixUrl;

    @XStreamAsAttribute
    private String parentId;

    @XStreamAsAttribute
    private String questionId;

    private Content content;

    private ContentRegion contentRegion;

    private List<Region> regionList;

}
