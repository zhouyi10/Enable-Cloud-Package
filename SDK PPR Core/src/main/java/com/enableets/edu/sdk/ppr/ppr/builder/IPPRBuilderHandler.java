package com.enableets.edu.sdk.ppr.ppr.builder;

import com.enableets.edu.sdk.ppr.ppr.builder.content.IContent;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;

/**
 * generate ppr
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public interface IPPRBuilderHandler {

    public PPR build(IContent content);
}
