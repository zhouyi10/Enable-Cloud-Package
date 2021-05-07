package com.enableets.edu.sdk.ppr.ppr.action;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * PPR action exception
 * @author walle_yu@enable-ets.com
 * @since 2020/07/15
 **/
public class PPRActionHandlerException extends PPRException {
    public PPRActionHandlerException(String message) {
        super(message);
    }

    public PPRActionHandlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRActionHandlerException(Throwable cause) {
        super(cause);
    }
}
