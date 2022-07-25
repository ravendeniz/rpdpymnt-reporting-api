package com.rpdpymnt.reporting.exception;

import java.io.Serializable;

public class ApiException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 792284004348824687L;

    public ApiException(String msg) {
        super(msg);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
