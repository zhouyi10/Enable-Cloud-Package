package com.enableets.edu.sdk.ppr.ppr.parse;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.parse.resources.IPPRResource;

/**
 * @Date 2020/07/14 16:13
 * @Author caleb_liu@enable-ets.com
 **/

public class PPRParseHandler extends AbstractPPRParseHandler {

    public PPRParseHandler(Configuration configuration) {
        super(configuration);
    }

    @Override
    public PPRInfoBO parse(IPPRResource parseResource) throws PPRVersionMismatchException {
        return tranPPRInfoXML(parseResource.setConfiguration(configuration).parsePPR());
    }

    @Override
    public PPRInfoBO parseIgnore(IPPRResource parseResource) {
        parseResource.setIgnoreVersion(Boolean.TRUE);
        try {
            return tranPPRInfoXML(parseResource.setConfiguration(configuration).parsePPR());
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PPRInfoBO receive(IPPRResource parseResource) {
        return receive(parseResource, Boolean.TRUE);
    }

    @Override
    public PPRInfoBO receive(IPPRResource parseResource, boolean ignoreVersion) {
        PPRInfoBO ppr = null;
        if (ignoreVersion){
            try {
                ppr = this.parse(parseResource);
            } catch (PPRVersionMismatchException e) {
                e.printStackTrace();
            }
        }else{
            ppr = this.parseIgnore(parseResource);
        }
        return ppr;
    }

}
