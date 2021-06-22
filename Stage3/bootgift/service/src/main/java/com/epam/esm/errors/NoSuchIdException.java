package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoSuchIdException extends LocalAppException{
    static {
        code = 42L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchIdException(){
        super();
    }
    public NoSuchIdException(String s){
        super(s);
    }
}
