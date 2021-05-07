package com.enableets.edu.pakage.book.bean.body;


import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("contentRegion")
public class ContentRegion {

    @XStreamAsAttribute
    private String x;

    @XStreamAsAttribute
    private String y;

    @XStreamAsAttribute
    private String width;

    @XStreamAsAttribute
    private String height;

}
