package com.nixsolutions.clouds.vkazakov.aws.exception;

@SuppressWarnings("serial")
public class InvalidFileUploadException extends ServiceException {
    public InvalidFileUploadException() {
        super();
    }

    public InvalidFileUploadException(String message) {
        super(message);
    }

    public InvalidFileUploadException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
