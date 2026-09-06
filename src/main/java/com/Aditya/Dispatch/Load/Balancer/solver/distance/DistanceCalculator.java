package com.Aditya.Dispatch.Load.Balancer.solver.distance;

public interface DistanceCalculator {
    
    /**
     * Calculates the great-circle distance between two geographic coordinates in kilometers.
     *
     * @param lat1 Latitude of first point in degrees
     * @param lon1 Longitude of first point in degrees
     * @param lat2 Latitude of second point in degrees
     * @param lon2 Longitude of second point in degrees
     * @return Distance in kilometers
     */
    double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2);
}
