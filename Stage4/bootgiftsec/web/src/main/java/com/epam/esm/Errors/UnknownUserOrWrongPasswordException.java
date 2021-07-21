package com.epam.esm.Errors;

import com.epam.esm.errors.LocalAppException;
import org.springframework.http.HttpStatus;

public class UnknownUserOrWrongPasswordException extends LocalAppException{
    static {
        code = 478L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public UnknownUserOrWrongPasswordException(){
        super("Unknown user or wrong password.");
    }
}
