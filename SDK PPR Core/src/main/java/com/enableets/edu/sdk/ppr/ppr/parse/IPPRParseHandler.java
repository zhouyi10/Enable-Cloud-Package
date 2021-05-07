package com.enableets.edu.sdk.ppr.ppr.parse;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.parse.resources.IPPRResource;

/**
 * @Date 2020/07/14$ 16:08$
 * @Author caleb_liu@enable-ets.com
 **/

public interface IPPRParseHandler {

    PPRInfoBO parse(IPPRResource parseResource) throws PPRVersionMismatchException;

    PPRInfoBO parseIgnore(IPPRResource parseResource);

    PPRInfoBO receive(IPPRResource parseResource);

    PPRInfoBO receive(IPPRResource parseResource, boolean ignoreVersion);
}
