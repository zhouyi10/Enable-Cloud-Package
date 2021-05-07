package com.enableets.edu.sdk.ppr.ppr.parse.resources;

import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.core.PPRInfoXML;

/**
 * @Date 2020/07/14 16:55
 * @Author caleb_liu@enable-ets.com
 **/

public interface IPPRResource {

    PPRInfoXML parsePPR() throws PPRVersionMismatchException;

    IPPRResource setConfiguration(Configuration configuration);

    void setIgnoreVersion(Boolean ignoreVersion);

    String getPPRRootPath();
}
