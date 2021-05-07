package com.enableets.edu.sdk.ppr.ppr.builder;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.builder.content.IContent;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public abstract class AbstractPPRBuilderHandler implements IPPRBuilderHandler{

    protected final Configuration configuration;

    public AbstractPPRBuilderHandler(Configuration configuration){
        this.configuration = configuration;
    }

    public abstract PPR build(IContent content);
}
