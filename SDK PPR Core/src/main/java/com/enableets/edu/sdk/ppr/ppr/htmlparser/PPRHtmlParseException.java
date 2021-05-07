package com.enableets.edu.sdk.ppr.ppr.htmlparser;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/23
 **/
public class PPRHtmlParseException extends PPRException {
    public PPRHtmlParseException(String message) {
        super(message);
    }

    public PPRHtmlParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRHtmlParseException(Throwable cause) {
        super(cause);
    }
}
