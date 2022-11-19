package com.nhnacademy.nhnmart.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super("Already existed user: " + message);
    }
}
