package com.enableets.edu.sdk.ppr.ppr.htmlparser.scanner;

import org.htmlparser.Parser;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class ParserFactory {

    private static Parser parser;

    public static Parser instance(){
        if (parser == null) parser = new Parser();
        return parser;
    }
}
