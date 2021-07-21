package com.epam.esm.Errors;

import com.epam.esm.errors.LocalAppException;
import org.springframework.http.HttpStatus;

public class NoPermissionsForResourceException extends LocalAppException{
    static {
        code = 403L;
        httpStatus = HttpStatus.BAD_REQUEST;
    }
    public NoPermissionsForResourceException(){
        super("There is no any permission for this resource.");
    }
}
