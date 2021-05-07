package com.enableets.edu.pakage.core.core.http;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class PakcageHttpClientException extends PackageException {

    public PakcageHttpClientException(String message) {
        super(message);
    }

    public PakcageHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PakcageHttpClientException(Throwable cause) {
        super(cause);
    }
}
