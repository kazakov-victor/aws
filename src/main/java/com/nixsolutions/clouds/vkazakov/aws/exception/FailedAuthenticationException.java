package com.nixsolutions.clouds.vkazakov.aws.exception;

public class FailedAuthenticationException extends ServiceException {
    public FailedAuthenticationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
