package com.rpdpymnt.reporting.exception;

public class InvalidParameterException extends RuntimeException {

    private static final long serialVersionUID = 1781514417283638870L;

    public InvalidParameterException(String message) {
        super(message);
    }

    public InvalidParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
