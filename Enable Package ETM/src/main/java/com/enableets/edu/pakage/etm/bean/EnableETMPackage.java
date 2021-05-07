package com.enableets.edu.pakage.etm.bean;

import com.enableets.edu.pakage.core.bean.Files;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.etm.bean.body.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XStreamAlias("content")
@NoArgsConstructor
@AllArgsConstructor
public class EnableETMPackage implements IEnableXmlPackage {


    private Header header;

    private ETMBody body;

    private Files files;

    @Override
    public Class[] buildXmlClasses() {
        return new Class[]{EnableETMPackage.class, Header.class, Property.class, ETMBody.class, Content.class, BodyItem.class, Page.class, Region.class};
    }

    public EnableETMPackage(Header header, ETMBody body) {
        this.header = header;
        this.body = body;
    }
}



