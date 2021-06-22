package com.epam.esm.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class NotExistEndPointException extends Exception{
    @Override
    public String getMessage() {
        return "Such End-Point doesn't exist.";
    }
}
