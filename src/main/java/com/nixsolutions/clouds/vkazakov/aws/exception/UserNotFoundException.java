package com.nixsolutions.clouds.vkazakov.aws.exception;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
