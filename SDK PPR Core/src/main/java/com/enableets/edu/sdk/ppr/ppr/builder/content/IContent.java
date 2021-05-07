package com.enableets.edu.sdk.ppr.ppr.builder.content;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public interface IContent {

    /**
     *
     */
    public PPR build();

    /**
     *
     */
    public void setConfiguration(Configuration configuration);
}
