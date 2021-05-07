package com.enableets.edu.sdk.ppr.core;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class PPRBaseException extends PPRException {
    public PPRBaseException(String message) {
        super(message);
    }

    public PPRBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRBaseException(Throwable cause) {
        super(cause);
    }
}
