package com.kapuza.springSecurity.exceptionsHandling;

public class UserValidationException extends RuntimeException {
    public UserValidationException(String message) {
        super(message);
    }
}
