package com.globo.application.exceptions;

public class CustomException extends RuntimeException {

    public CustomException(String msg) {
        super(msg, new Throwable(), false, false);
    }
}
