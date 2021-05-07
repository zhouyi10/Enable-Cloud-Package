package com.enableets.edu.pakage.ppr.action;

import com.enableets.edu.pakage.core.exception.PackageException;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/18
 **/
public class PPRPackageLifecycleException extends PackageException {

    public PPRPackageLifecycleException(String message) {
        super(message);
    }

    public PPRPackageLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public PPRPackageLifecycleException(Throwable cause) {
        super(cause);
    }
}
