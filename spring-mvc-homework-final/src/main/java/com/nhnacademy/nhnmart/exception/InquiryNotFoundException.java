package com.nhnacademy.nhnmart.exception;

public class InquiryNotFoundException extends RuntimeException {
    public InquiryNotFoundException(String message) {
        super("Inquiry not founded. InquiryId : " + message);
    }
}
