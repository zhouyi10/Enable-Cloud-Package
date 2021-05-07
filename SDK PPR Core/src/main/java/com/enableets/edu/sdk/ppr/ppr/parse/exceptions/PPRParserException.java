package com.enableets.edu.sdk.ppr.ppr.parse.exceptions;

import com.enableets.edu.sdk.ppr.exceptions.PPRException;

/**
 * @Date 2020/07/01$ 17:39$
 * @Author caleb_liu@enable-ets.com
 **/

public class PPRParserException extends PPRException {
    public PPRParserException(String message) {
        super(message);
    }

    public PPRParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRParserException(Throwable cause) {
        super(cause);
    }
}
