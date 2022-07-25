package com.rpdpymnt.reporting.exception;

public class InvalidPreconditionException extends ApiException {

    private static final long serialVersionUID = -7519745749096776992L;

    public InvalidPreconditionException(String msg) {
        super(msg);
    }

    public InvalidPreconditionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
