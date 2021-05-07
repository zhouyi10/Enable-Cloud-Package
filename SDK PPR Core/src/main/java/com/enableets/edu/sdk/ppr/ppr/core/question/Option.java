package com.enableets.edu.sdk.ppr.ppr.core.question;

import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
@Data
@XStreamAlias("options")
public class Option extends EntityToXml {

    public Option(){
        this.optionTitle = "";
        this.optionContent = "";
    }

    public Option(String optionContent, String optionId, String optionOrder, String optionTitle){
        if (optionContent == null) optionContent = "";
        if (optionTitle == null) optionTitle = "";
        this.optionContent = optionContent;
        this.optionId = optionId;
        this.optionOrder = optionOrder;
        this.optionTitle = optionTitle;
    }

    @XStreamAsAttribute
    private String optionContent;

    @XStreamAsAttribute
    private String optionId;

    @XStreamAsAttribute
    private String optionOrder;

    @XStreamAsAttribute
    private String optionTitle;
}
