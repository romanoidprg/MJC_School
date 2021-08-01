package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoTagWithSuchNameException extends LocalAppException{
    static {
        code = 426L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoTagWithSuchNameException(){
        super();
    }
    public NoTagWithSuchNameException(String s){
        super(s);
    }
}
