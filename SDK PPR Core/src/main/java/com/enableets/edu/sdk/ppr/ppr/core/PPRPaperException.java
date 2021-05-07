package com.enableets.edu.sdk.ppr.ppr.core;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/16 16:47
 */

public class PPRPaperException extends PPRException {
    public PPRPaperException(String message) {
        super(message);
    }

    public PPRPaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRPaperException(Throwable cause) {
        super(cause);
    }
}
