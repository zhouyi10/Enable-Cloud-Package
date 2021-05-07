package com.enableets.edu.sdk.ppr.ppr.parse.exceptions;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @Date 2020/07/01$ 17:39$
 * @Author caleb_liu@enable-ets.com
 **/

public class PPRParserResourceException extends PPRException {
    public PPRParserResourceException(String message) {
        super(message);
    }

    public PPRParserResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRParserResourceException(Throwable cause) {
        super(cause);
    }
}
