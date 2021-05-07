package com.enableets.edu.pakage.etm.action;

import com.enableets.edu.pakage.core.exception.PackageException;


public class ETMPackageLifecycleException extends PackageException {

    public ETMPackageLifecycleException(String message) {
        super(message);
    }

    public ETMPackageLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ETMPackageLifecycleException(Throwable cause) {
        super(cause);
    }
}
