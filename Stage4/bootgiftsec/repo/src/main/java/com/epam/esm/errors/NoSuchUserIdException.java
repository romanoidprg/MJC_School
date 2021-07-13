package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoSuchUserIdException extends LocalAppException{
    static {
        code = 45L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchUserIdException(){
        super("The user doesn't exist.");
    }
    public NoSuchUserIdException(String id){
        super("The user with id [" + id + "] doesn't exist.");
    }
}
