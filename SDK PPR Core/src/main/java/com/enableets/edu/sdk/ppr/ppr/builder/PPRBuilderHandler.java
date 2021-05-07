package com.enableets.edu.sdk.ppr.ppr.builder;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.builder.content.IContent;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class PPRBuilderHandler extends AbstractPPRBuilderHandler {

    public PPRBuilderHandler(Configuration configuration){
        super(configuration);
    }

    @Override
    public PPR build(IContent content) {
        content.setConfiguration(configuration);
        return content.build();
    }
}
