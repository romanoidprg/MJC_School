package com.epam.esm.errors;

import org.springframework.http.HttpStatus;

public class IncorrectAmountOfCertFieldsException extends LocalAppException{
    static {
        code = 46L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public IncorrectAmountOfCertFieldsException(){
        super("There must be only one field of certificate to change.");
    }
}
