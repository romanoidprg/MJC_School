package com.epam.esm.controllers;

import com.epam.esm.errors.ErrorResponse;
import com.epam.esm.errors.LocalAppException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorsHandler {
    private static final String DEFAULT_CODE = "00";
    Logger logger = LogManager.getLogger(ErrorsHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(HttpServletRequest req, Exception e) {
        logger.error(e.getMessage());
        ErrorResponse erRsp;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String code = DEFAULT_CODE;
        if (e instanceof LocalAppException) {
            status = ((LocalAppException) e).getHttpStatus();
            code = String.valueOf(((LocalAppException) e).getCode());
        }
        erRsp = new ErrorResponse(e.getMessage() + " on URL: " + req.getRequestURL(),
                status.value() + code);
        return new ResponseEntity<>(erRsp, status);
    }
}
