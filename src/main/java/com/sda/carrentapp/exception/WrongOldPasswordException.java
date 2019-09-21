package com.sda.carrentapp.exception;

public class WrongOldPasswordException extends RuntimeException {
    public WrongOldPasswordException() {
        super();
    }

    @Override
    public String getMessage() {
        return "Wrong old password";
    }
}
