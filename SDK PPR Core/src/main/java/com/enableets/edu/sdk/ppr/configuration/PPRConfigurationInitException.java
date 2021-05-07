package com.enableets.edu.sdk.ppr.configuration;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/15
 **/
public class PPRConfigurationInitException extends PPRException {
    public PPRConfigurationInitException(String message) {
        super(message);
    }

    public PPRConfigurationInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRConfigurationInitException(Throwable cause) {
        super(cause);
    }
}
