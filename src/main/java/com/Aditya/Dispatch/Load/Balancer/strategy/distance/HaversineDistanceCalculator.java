package com.Aditya.Dispatch.Load.Balancer.strategy.distance;

import org.springframework.stereotype.Component;

/**
 * Thread-safe, stateless implementation of Haversine distance calculator.
 * Calculates great-circle distances between points on Earth in kilometers.
 */
@Component
public class HaversineDistanceCalculator implements DistanceCalculator {

    public static final double EARTH_RADIUS_KM = 6371.0088;

    @Override
    public double calculateDistanceKm(double lat1, double lon1, double lat2, double lon2) {
        // Fast path for identical coordinates or floating point equality
        if (Double.compare(lat1, lat2) == 0 && Double.compare(lon1, lon2) == 0) {
            return 0.0;
        }

        // Clamp latitude to [-90, 90] and longitude to [-180, 180]
        double clampedLat1 = Math.min(90.0, Math.max(-90.0, lat1));
        double clampedLat2 = Math.min(90.0, Math.max(-90.0, lat2));

        double dLat = Math.toRadians(clampedLat2 - clampedLat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double rLat1 = Math.toRadians(clampedLat1);
        double rLat2 = Math.toRadians(clampedLat2);

        double a = Math.pow(Math.sin(dLat / 2.0), 2) +
                   Math.cos(rLat1) * Math.cos(rLat2) *
                   Math.pow(Math.sin(dLon / 2.0), 2);

        // Clamping to prevent NaN due to floating-point rounding
        a = Math.min(1.0, Math.max(0.0, a));
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return EARTH_RADIUS_KM * c;
    }
}
