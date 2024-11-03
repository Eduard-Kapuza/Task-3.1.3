package com.kapuza.springSecurity.exceptionsHandling;

public class NotFoundUserException extends RuntimeException{
    public NotFoundUserException(String message) {
        super(message);
    }
}
