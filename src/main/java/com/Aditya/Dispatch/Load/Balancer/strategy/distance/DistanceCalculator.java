package com.Aditya.Dispatch.Load.Balancer.strategy.distance;

public interface DistanceCalculator {
    double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2);
}
