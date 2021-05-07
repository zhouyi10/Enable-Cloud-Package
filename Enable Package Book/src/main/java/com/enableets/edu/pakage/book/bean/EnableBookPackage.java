package com.enableets.edu.pakage.book.bean;

import com.enableets.edu.pakage.book.bean.body.*;
import com.enableets.edu.pakage.core.bean.Files;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.enableets.edu.pakage.core.bean.Property;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@XStreamAlias("content")
@NoArgsConstructor
@AllArgsConstructor
public class EnableBookPackage implements IEnableXmlPackage {


    private Header header;

    private BookBody body;

    private Files files;

    @Override
    public Class[] buildXmlClasses() {
        return new Class[]{EnableBookPackage.class, Header.class, Property.class, BookBody.class, Content.class, BodyItem.class, ContentRegion.class, Page.class, Region.class};
    }

    public EnableBookPackage(Header header, BookBody body) {
        this.header = header;
        this.body = body;
    }
}



