package com.sda.carrentapp.exception;

public class EmailsAreNotEqualException extends RuntimeException {
    public EmailsAreNotEqualException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Wrong email given";
    }
}
