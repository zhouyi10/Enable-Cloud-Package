package com.enableets.edu.sdk.ppr.ppr.builder.content.Exceptions;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class PPRContentException extends PPRException {

    public PPRContentException(String message) {
        super(message);
    }

    public PPRContentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRContentException(Throwable cause) {
        super(cause);
    }
}
