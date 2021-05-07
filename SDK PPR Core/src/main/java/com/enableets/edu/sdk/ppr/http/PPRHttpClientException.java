package com.enableets.edu.sdk.ppr.http;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class PPRHttpClientException extends PPRException {

    public PPRHttpClientException(String message) {
        super(message);
    }

    public PPRHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRHttpClientException(Throwable cause) {
        super(cause);
    }
}
