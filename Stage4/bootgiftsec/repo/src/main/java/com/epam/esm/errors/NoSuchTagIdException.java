package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoSuchTagIdException extends LocalAppException{
    static {
        code = 46L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchTagIdException(){
        super("The tag doesn't exist.");
    }
    public NoSuchTagIdException(String id){
        super("The tag with id [" + id + "] doesn't exist.");
    }
}
