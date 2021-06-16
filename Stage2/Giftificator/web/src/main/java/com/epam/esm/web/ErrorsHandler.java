package com.epam.esm.web;

import com.epam.esm.errors.ErrorResponse;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.errors.NotExistEndPointException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleNoExistEndPointException(HttpServletRequest req, Exception e) {
        CodeAndStatus cAS = getExIntCodeAndStatus(e);
        ErrorResponse erRsp = new ErrorResponse(e.getMessage() + " on URL: " + req.getRequestURL(),
                cAS.status.value() + cAS.code);
        return new ResponseEntity<>(erRsp, cAS.status);    }

    private CodeAndStatus getExIntCodeAndStatus(Exception e) {
        if (e instanceof NoSuchIdException) {
            return new CodeAndStatus("41", HttpStatus.BAD_REQUEST);
        }
        if (e instanceof NotExistEndPointException) {
            return new CodeAndStatus("42", HttpStatus.BAD_REQUEST);
        }
        return new CodeAndStatus("00", HttpStatus.OK);
    }

    private static class CodeAndStatus {
        private final String code;
        private final HttpStatus status;

        private CodeAndStatus(String code, HttpStatus status) {
            this.code = code;
            this.status = status;
        }
    }
}
