package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/20
 **/
public interface IXmlParseHandler {

    public <T,B> T parse(IXmlParser<T,B> parser) throws PPRVersionMismatchException;

    public <T,B> T parseIgnore(IXmlParser<T,B> parser);

    public <T,B> B parseBO(IXmlParser<T,B> parser) throws PPRVersionMismatchException;

    public <T,B> B parseBOIgnore(IXmlParser<T,B> parser);
}
