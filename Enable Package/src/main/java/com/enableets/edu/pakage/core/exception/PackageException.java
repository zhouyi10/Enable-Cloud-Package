package com.enableets.edu.pakage.core.exception;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class PackageException extends RuntimeException  {

    public PackageException(String message){
        super(message);
    }

    public PackageException(String message, Throwable cause){
        super(message, cause);
    }

    public PackageException(Throwable cause){
        super(cause);
    }
}
