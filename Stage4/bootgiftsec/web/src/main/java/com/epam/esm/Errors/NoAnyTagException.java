package com.epam.esm.Errors;

import com.epam.esm.errors.LocalAppException;
import org.springframework.http.HttpStatus;

public class NoAnyTagException extends LocalAppException{
    static {
        code = 499L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoAnyTagException(){
        super("There is no any 'tag' parameter in HTTP request");
    }
}
