package com.sda.carrentapp.exception;

public class EmailsAreNotEqualException extends Exception {
    public EmailsAreNotEqualException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Wrong email given";
    }
}
