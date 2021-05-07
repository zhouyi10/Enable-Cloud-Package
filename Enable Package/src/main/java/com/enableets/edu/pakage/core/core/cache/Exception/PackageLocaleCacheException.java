package com.enableets.edu.pakage.core.core.cache.Exception;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/25
 **/
public class PackageLocaleCacheException extends PackageException {

    public PackageLocaleCacheException(String message) {
        super(message);
    }

    public PackageLocaleCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageLocaleCacheException(Throwable cause) {
        super(cause);
    }
}
