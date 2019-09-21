package com.sda.carrentapp.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Passwords do not match";
    }
}
