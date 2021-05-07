package com.enableets.edu.sdk.ppr.ppr.parse;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.core.PPRInfoXML;
import com.enableets.edu.sdk.ppr.utils.XMLObject2Entity;

import java.util.Map;

/**
 * @Date 2020/07/14 16:07
 * @Author caleb_liu@enable-ets.com
 **/

public abstract class AbstractPPRParseHandler implements IPPRParseHandler{

    protected final Configuration configuration;

    protected Map<String, com.enableets.edu.sdk.ppr.ppr.core.question.Question> questionMap;

    public AbstractPPRParseHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    protected PPRInfoBO tranPPRInfoXML(PPRInfoXML pprInfoXML) {
        return new PPRInfoBO(XMLObject2Entity.tranPaperXML(pprInfoXML.getPaper()), XMLObject2Entity.tranPaperCardXML(pprInfoXML.getPaperCard()));
    }

}
