package com.enableets.edu.sdk.ppr.exceptions;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/20
 **/
public class PPRVersionMismatchException extends Exception{

    public PPRVersionMismatchException() {
    }

    public PPRVersionMismatchException(String message) {
        super(message);
    }

    public PPRVersionMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRVersionMismatchException(Throwable cause) {
        super(cause);
    }

    public PPRVersionMismatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
