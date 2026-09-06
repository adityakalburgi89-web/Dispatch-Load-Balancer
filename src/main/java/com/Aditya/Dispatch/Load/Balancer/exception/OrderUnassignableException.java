package com.Aditya.Dispatch.Load.Balancer.exception;

public class OrderUnassignableException extends RuntimeException {

    private final String code = "ORDER_UNASSIGNABLE";

    public OrderUnassignableException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
