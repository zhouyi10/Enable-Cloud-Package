package com.enableets.edu.sdk.ppr.xml.xpath;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/24
 **/
public class PPRXPathException extends PPRException {
    public PPRXPathException(String message) {
        super(message);
    }

    public PPRXPathException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRXPathException(Throwable cause) {
        super(cause);
    }
}
