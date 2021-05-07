package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.xml.xpath.XPathParser;
import com.enableets.edu.sdk.ppr.xml.xpath.XPathParserFactory;

import java.io.File;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public abstract class AbstractXmlParser<T,B> implements IXmlParser<T,B> {

    public XPathParser xPathParser;

    public final CommonNodeParse commonNodeParse;

    public AbstractXmlParser(String xml){
        if (StringUtils.isNotBlank(xml)){
            xPathParser = XPathParserFactory.buildToString(xml);
        }
        commonNodeParse = new CommonNodeParse();
    }

    public AbstractXmlParser(File file){
        xPathParser = XPathParserFactory.buildToFile(file.toString());
        commonNodeParse = new CommonNodeParse();
    }

    protected void setIgnoreVersion(boolean ignoreVersion){
        commonNodeParse.setIgnoreVersion(ignoreVersion);
    }

    public T parseIgnore(){
        try {
            setIgnoreVersion(Boolean.TRUE);
            return parse();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    public B parseBOIgnore(){
        setIgnoreVersion(Boolean.TRUE);
        try {
            return parseBO();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }
}
