package com.enableets.edu.pakage.book.action;

import com.enableets.edu.pakage.core.exception.PackageException;


public class BookPackageLifecycleException extends PackageException {

    public BookPackageLifecycleException(String message) {
        super(message);
    }

    public BookPackageLifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookPackageLifecycleException(Throwable cause) {
        super(cause);
    }
}
