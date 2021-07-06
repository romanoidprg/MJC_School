package com.epam.esm.Errors;

import com.epam.esm.errors.LocalAppException;
import org.springframework.http.HttpStatus;

public class NoSuchPageException extends LocalAppException{
    static {
        code = 48L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoSuchPageException(){
        super("The page doesn't exist.");
    }
    public NoSuchPageException(String id){
        super("The page number [" + id + "] doesn't exist.");
    }
}
