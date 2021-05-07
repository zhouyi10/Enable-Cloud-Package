package com.enableets.edu.sdk.ppr.ppr.action;

import com.enableets.edu.sdk.ppr.configuration.Configuration;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public abstract class AbstractPPRActionHandler implements IPPRActionHandler{

    public final Configuration configuration;

    public AbstractPPRActionHandler(Configuration configuration) {
        this.configuration = configuration;
    }
}
