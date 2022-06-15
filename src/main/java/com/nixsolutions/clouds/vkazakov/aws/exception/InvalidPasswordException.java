package com.nixsolutions.clouds.vkazakov.aws.exception;

public class InvalidPasswordException extends ServiceException {
    public InvalidPasswordException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
