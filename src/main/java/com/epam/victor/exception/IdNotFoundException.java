package com.epam.victor.exception;

public class IdNotFoundException extends RuntimeException{

    public IdNotFoundException() {
    }

    public IdNotFoundException(String message) {
        super(message);
    }

    public IdNotFoundException(Throwable cause) {
        super(cause);
    }
}
