package com.epam.esm.Errors;

import com.epam.esm.errors.LocalAppException;
import org.springframework.http.HttpStatus;

public class UnknownSortParametrException extends LocalAppException{
    static {
        code = 49L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public UnknownSortParametrException(){
        super("Unknown sort parameter.");
    }
    public UnknownSortParametrException(String param){
        super("Unknown sort parameter - [" + param + "].");
    }
}
