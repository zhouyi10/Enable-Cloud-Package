package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;

import java.io.File;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class PaperCardXmlIgnoreVersionParser extends PaperCardXmlParser {

    public PaperCardXmlIgnoreVersionParser(String paperCardXmlStr){
        super(paperCardXmlStr);
        setIgnoreVersion(Boolean.TRUE);
    }

    public PaperCardXmlIgnoreVersionParser(File file){
        super(file);
        setIgnoreVersion(Boolean.TRUE);
    }

    @Override
    public PaperCardXML parse() {
        try {
            return super.parse();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CardBO parseBO() {
        try {
            return super.parseBO();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }
}
