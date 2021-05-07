package com.enableets.edu.sdk.ppr;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.http.IHttpClient;
import com.enableets.edu.sdk.ppr.ppr.action.IPPRActionHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.IPPRBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.IPaperCardBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.IPPRParseHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.xml.IXmlParseHandler;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public interface IPprHandlerFactory {

    public IHttpClient getHttpClient();

    public IPPRBuilderHandler getPprBuilderHandler();

    public IPaperCardBuilderHandler getPaperCardBuilderHandler();

    public IPPRParseHandler getPprParseHandler();

    public IXmlParseHandler getXmlParseHandler();

    public IPPRActionHandler getPprActionHandler();

    public Configuration getConfiguration();

}
