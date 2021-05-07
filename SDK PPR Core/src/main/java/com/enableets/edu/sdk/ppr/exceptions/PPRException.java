package com.enableets.edu.sdk.ppr.exceptions;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
public abstract class PPRException extends RuntimeException {

    public PPRException(String message){
        super(message);
    }

    public PPRException(String message, Throwable cause){
        super(message, cause);
    }

    public PPRException(Throwable cause){
        super(cause);
    }
}
