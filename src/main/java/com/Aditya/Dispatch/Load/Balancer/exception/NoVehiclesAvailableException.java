package com.Aditya.Dispatch.Load.Balancer.exception;

public class NoVehiclesAvailableException extends RuntimeException {

    private final String code = "NO_VEHICLES_AVAILABLE";

    public NoVehiclesAvailableException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}
