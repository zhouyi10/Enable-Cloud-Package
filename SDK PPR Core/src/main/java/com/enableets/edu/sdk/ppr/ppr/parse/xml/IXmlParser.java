package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public interface IXmlParser<T,B> {

    T parse() throws PPRVersionMismatchException;

    T parseIgnore();

    B parseBO() throws PPRVersionMismatchException;

    B parseBOIgnore();
}
