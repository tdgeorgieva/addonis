package com.addonis.exceptions;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String message) {
        super(message);
    }

    public UnauthorizedOperationException() {
    }
}
