package com.Aditya.Dispatch.Load.Balancer.model.enums;

public enum Priority {
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    private final int weight;

    Priority(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
