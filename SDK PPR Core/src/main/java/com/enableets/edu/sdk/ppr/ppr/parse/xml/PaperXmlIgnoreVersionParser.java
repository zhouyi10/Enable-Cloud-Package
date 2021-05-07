package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;

import java.io.File;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class PaperXmlIgnoreVersionParser extends PaperXmlParser {

    public PaperXmlIgnoreVersionParser(String paperXml){
        super(paperXml);
        setIgnoreVersion(Boolean.TRUE);
    }

    public PaperXmlIgnoreVersionParser(File file){
        super(file);
        setIgnoreVersion(Boolean.TRUE);
    }

    @Override
    public PaperXML parse(){
        try {
            return super.parse();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PPRBO parseBO() {
        try {
            return super.parseBO();
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

}
