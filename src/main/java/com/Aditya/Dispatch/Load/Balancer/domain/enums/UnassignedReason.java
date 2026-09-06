package com.Aditya.Dispatch.Load.Balancer.domain.enums;

public enum UnassignedReason {
    EXCEEDS_MAX_SINGLE_VEHICLE_CAPACITY("Order package weight exceeds the maximum capacity of any single vehicle in the fleet"),
    FLEET_CAPACITY_EXHAUSTED("No remaining vehicle has sufficient capacity to carry this order"),
    NO_VEHICLES_AVAILABLE("No active vehicles registered in the system"),
    INVALID_INPUT_DATA("Order failed input validation checks");

    private final String description;

    UnassignedReason(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
