package com.laklu.pos.auth.exceptions;

// TODO: beatify this class
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException() {
        super("");
    }
}
