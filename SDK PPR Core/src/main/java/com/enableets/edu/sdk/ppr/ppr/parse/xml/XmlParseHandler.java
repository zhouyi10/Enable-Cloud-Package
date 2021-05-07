package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/20
 **/
public class XmlParseHandler implements IXmlParseHandler {

    @Override
    public <T, B> T parse(IXmlParser<T, B> parser) throws PPRVersionMismatchException {
        return parser.parse();
    }

    @Override
    public <T, B> T parseIgnore(IXmlParser<T, B> parser) {
        return parser.parseIgnore();
    }

    @Override
    public <T, B> B parseBO(IXmlParser<T, B> parser) throws PPRVersionMismatchException {
        return parser.parseBO();
    }

    @Override
    public <T, B> B parseBOIgnore(IXmlParser<T, B> parser) {
        return parser.parseBOIgnore();
    }
}
