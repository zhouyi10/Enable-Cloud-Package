package com.enableets.edu.pakage.core.core.http;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/19
 **/
public class packageHttpClientException extends PackageException {

    public packageHttpClientException(String message) {
        super(message);
    }

    public packageHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public packageHttpClientException(Throwable cause) {
        super(cause);
    }
}
