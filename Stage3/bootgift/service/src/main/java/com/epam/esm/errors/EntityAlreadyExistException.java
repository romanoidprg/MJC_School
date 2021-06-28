package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistException extends LocalAppException{
    static {
        code = 43L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    @Override
    public String getMessage() {
        return "Such entity already exist.";
    }
}
