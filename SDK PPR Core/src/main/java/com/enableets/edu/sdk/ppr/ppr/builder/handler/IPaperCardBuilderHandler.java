package com.enableets.edu.sdk.ppr.ppr.builder.handler;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public interface IPaperCardBuilderHandler {

    public String build(CardBO cardBO);

    public void build(CardBO cardBO, String destPath);
}
