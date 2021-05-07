package com.enableets.edu.sdk.ppr.ppr.action;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public interface IPPRActionHandler {

    public boolean save(CardBO cardBO);
}
