package com.enableets.edu.sdk.ppr.cache.Exception;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/28
 **/
public class PPRLocaleCacheException extends PPRException {
    public PPRLocaleCacheException(String message) {
        super(message);
    }

    public PPRLocaleCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRLocaleCacheException(Throwable cause) {
        super(cause);
    }
}
