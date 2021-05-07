package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.configuration.PPRSDKVersion;
import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.core.Header;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.xml.xpath.XNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class CommonNodeParse {

    private Boolean ignoreVersion = Boolean.FALSE;

    public Header parseHeader(XNode xNode) throws PPRVersionMismatchException {
        Header header = new Header();
        if (xNode == null) return header;
        String curVersion = PPRSDKVersion.get();
        String xmlVersion = xNode.evalString("./version");
        if (!curVersion.equals(xmlVersion) && !ignoreVersion){
            throw new PPRVersionMismatchException("SDK version is " + curVersion + "; but ppr version is " + xmlVersion + ", not match");
        }
        header.setVersion(xmlVersion);
        header.setEncoding(xNode.evalString("./encoding"));
        header.setVerification(xNode.evalString("./verification"));
        header.setEncryption(xNode.evalString("./encryption"));
        header.setCompression(xNode.evalString("./compression"));
        header.setProperty(parseHeaderProperty(xNode.evalNode("./property")));
        return header;
    }

    private Property parseHeaderProperty(XNode xNode){
        if (xNode == null) return new Property();
        List<Item> items = new ArrayList<>();
        List<XNode> xNodes = xNode.evalNodes("./item");
        for (XNode itemXNode : xNodes) {
            items.add(new Item(itemXNode.getAttribute("key"),  itemXNode.getBody()));
        }
        return new Property(items);
    }

    public void setIgnoreVersion(Boolean ignoreVersion){
        this.ignoreVersion = ignoreVersion;
    }
}
