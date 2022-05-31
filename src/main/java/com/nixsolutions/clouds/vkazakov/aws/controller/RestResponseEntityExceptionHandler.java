package com.nixsolutions.clouds.vkazakov.aws.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
        = { NumberFormatException.class, MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleWrongParameter(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Wrong path parameter!",
            new HttpHeaders(), HttpStatus.valueOf(422), request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Wrong argument!",
            new HttpHeaders(), HttpStatus.valueOf(422), request);
    }
}
