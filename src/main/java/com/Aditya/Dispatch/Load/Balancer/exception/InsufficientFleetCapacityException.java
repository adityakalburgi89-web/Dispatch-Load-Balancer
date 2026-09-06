package com.Aditya.Dispatch.Load.Balancer.exception;

public class InsufficientFleetCapacityException extends RuntimeException {

    private final String code = "INSUFFICIENT_FLEET_CAPACITY";

    public InsufficientFleetCapacityException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
