package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoSuchOrderIdException extends LocalAppException{
    static {
        code = 46L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchOrderIdException(){
        super("The order doesn't exist.");
    }
    public NoSuchOrderIdException(String id){
        super("The order with id [" + id + "] doesn't exist.");
    }
}
