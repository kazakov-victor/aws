package com.nixsolutions.clouds.vkazakov.aws.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
    extends ResponseEntityExceptionHandler {
    private final static String WRONG_PATH_PARAMETER_MESSAGE = "Wrong path parameter!";
    private final static String WRONG_ARGUMENT_MESSAGE = "Wrong argument!";

    @ExceptionHandler(value
        = { NumberFormatException.class, MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleWrongParameter(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, WRONG_PATH_PARAMETER_MESSAGE,
            new HttpHeaders(), HttpStatus.valueOf(422), request);
    }

    @ExceptionHandler(value = { IllegalArgumentException.class })
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, WRONG_ARGUMENT_MESSAGE,
            new HttpHeaders(), HttpStatus.valueOf(422), request);
    }
}
