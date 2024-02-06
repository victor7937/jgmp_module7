package com.epam.victor.exception;

public class ConsumerJsonException extends RuntimeException{
    public ConsumerJsonException() {
    }

    public ConsumerJsonException(String message) {
        super(message);
    }

    public ConsumerJsonException(Throwable cause) {
        super(cause);
    }
}
