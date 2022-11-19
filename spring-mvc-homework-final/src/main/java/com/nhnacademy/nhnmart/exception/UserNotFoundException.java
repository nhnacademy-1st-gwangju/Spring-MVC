package com.nhnacademy.nhnmart.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super("User not founded or invalid id/pwd: " + message);
    }
}
