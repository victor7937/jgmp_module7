package com.epam.victor.exception;

public class IncorrectMessageException extends RuntimeException {
    public IncorrectMessageException() {
    }

    public IncorrectMessageException(String message) {
        super(message);
    }
}
