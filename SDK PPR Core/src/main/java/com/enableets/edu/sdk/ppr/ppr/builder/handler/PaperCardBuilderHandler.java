package com.enableets.edu.sdk.ppr.ppr.builder.handler;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class PaperCardBuilderHandler extends AbstractPaperCardBuilderHandler {

    @Override
    public String build(CardBO cardBO) {
        return new PaperCardXmlBuildDirector(cardBO).getPaperCardXml();
    }

    @Override
    public void build(CardBO paperCardBO, String destPath) {
        new PaperCardXmlBuildDirector(paperCardBO).writePaperCardToFile(destPath);
    }
}
