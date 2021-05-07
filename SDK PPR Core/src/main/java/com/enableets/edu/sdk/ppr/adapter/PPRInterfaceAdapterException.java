package com.enableets.edu.sdk.ppr.adapter;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/22
 **/
public class PPRInterfaceAdapterException extends PPRException {

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
