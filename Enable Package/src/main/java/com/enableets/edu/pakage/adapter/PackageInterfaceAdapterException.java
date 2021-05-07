package com.enableets.edu.pakage.adapter;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class PackageInterfaceAdapterException extends PackageException {
    public PackageInterfaceAdapterException(String message) {
        super(message);
    }

    public PackageInterfaceAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageInterfaceAdapterException(Throwable cause) {
        super(cause);
    }
}
