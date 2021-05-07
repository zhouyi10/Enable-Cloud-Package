package com.enableets.edu.pakage.core.core.htmlparser;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class PackageHtmlParseException extends PackageException {
    public PackageHtmlParseException(String message) {
        super(message);
    }

    public PackageHtmlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackageHtmlParseException(Throwable cause) {
        super(cause);
    }
}
