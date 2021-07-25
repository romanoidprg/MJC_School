package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class NoSuchCertIdException extends LocalAppException{
    static {
        code = 44L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchCertIdException(){
        super("The certificate doesn't exist.");
    }
    public NoSuchCertIdException(String id){
        super("The certificate with id [" + id + "] doesn't exist.");
    }
}
