package com.Aditya.Dispatch.Load.Balancer.exception;

public class NoOrdersAvailableException extends RuntimeException {

    private final String code = "NO_ORDERS_AVAILABLE";

    public NoOrdersAvailableException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
