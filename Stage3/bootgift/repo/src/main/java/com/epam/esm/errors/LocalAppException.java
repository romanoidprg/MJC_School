package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public abstract class LocalAppException extends Exception {
    protected static long code;
    protected static HttpStatus httpStatus;

    public LocalAppException() {
        super();
    }

    public LocalAppException(String s) {
        super(s);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public long getCode() {
        return code;
    }
}
