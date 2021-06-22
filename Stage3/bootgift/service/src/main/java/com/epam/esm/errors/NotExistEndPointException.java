package com.epam.esm.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class NotExistEndPointException extends LocalAppException{

    static {
        code = 41L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getMessage() {
        return "Such End-Point doesn't exist.";
    }
}
