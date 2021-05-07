package com.enableets.edu.sdk.ppr.ppr.action;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.action.invoker.PaperCardBOInvoker;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.PaperCardBuilderHandler;

/**
 * PPR action handler
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class PPRActionHandler extends AbstractPPRActionHandler{

    private final PaperCardBuilderHandler paperCardBuilderHandler;

    public PPRActionHandler(Configuration configuration){
        super(configuration);
        paperCardBuilderHandler = new PaperCardBuilderHandler();
    }

    @Override
    public boolean save(CardBO cardBO){
        if (!paramVerification(cardBO)){
            throw new PPRActionHandlerException("parameter missing");
        }
        return new PaperCardBOInvoker(cardBO, configuration, paperCardBuilderHandler).save();
    }

    /**
     * PaperCardBO parameters verification
     * @return
     */
    private boolean paramVerification(CardBO cardBO){
        if (cardBO.getActions() == null || cardBO.getActions().size() < 3) return false;
        if (cardBO.getAnswers() == null || cardBO.getAnswers().size() == 0) return false;
        return Boolean.TRUE;
    }
}
