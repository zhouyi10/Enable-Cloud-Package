package com.enableets.edu.sdk.ppr.ppr.builder.handler;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXmlUtils;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/01
 **/
public class PaperCardXmlBuildDirector {

    private AbstractPaperCardBuilder handler = new PaperCardBuilder();

    public PaperCardXmlBuildDirector(CardBO cardBO){
        handler.setPaperCard(cardBO);
    }

    public void writePaperCardToFile(String filePath){
        handler.createHeader();
        handler.createBody();
        EntityToXmlUtils.toFile(handler.getPaperCardXMl(), filePath);
    }

    public String getPaperCardXml(){
        handler.createHeader();
        handler.createBody();
        return EntityToXmlUtils.toXml(handler.getPaperCardXMl());
    }
}
