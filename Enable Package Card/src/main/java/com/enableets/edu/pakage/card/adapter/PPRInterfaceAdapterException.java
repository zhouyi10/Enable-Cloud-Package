package com.enableets.edu.pakage.card.adapter;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/23
 **/
public class PPRInterfaceAdapterException extends PackageException {

    public PPRInterfaceAdapterException(String message) {
        super(message);
    }

    public PPRInterfaceAdapterException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRInterfaceAdapterException(Throwable cause) {
        super(cause);
    }
}
