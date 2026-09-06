package com.Aditya.Dispatch.Load.Balancer.exception;

public class DuplicateIdException extends RuntimeException {

    private final String code;

    public DuplicateIdException(String message) {
        super(message);
        this.code = "DUPLICATE_ID";
    }

    public DuplicateIdException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
