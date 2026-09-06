package com.Aditya.Dispatch.Load.Balancer.solver.distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HaversineDistanceCalculatorTest {

    private HaversineDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    @DisplayName("Should return 0.0 distance for identical coordinates")
    void testIdenticalCoordinates() {
        double distance = calculator.calculateDistanceKm(37.7749, -122.4194, 37.7749, -122.4194);
        assertEquals(0.0, distance, 1e-6);
    }

    @Test
    @DisplayName("Should correctly calculate distance between San Francisco and New York (~4129 km)")
    void testKnownDistanceSFToNYC() {
        // SF: 37.7749, -122.4194 | NYC: 40.7128, -74.0060
        double distance = calculator.calculateDistanceKm(37.7749, -122.4194, 40.7128, -74.0060);
        assertEquals(4129.0, distance, 30.0); // Within 30 km accuracy tolerance
    }

    @Test
    @DisplayName("Should handle North Pole to South Pole boundary calculations (~20015 km)")
    void testPolesDistance() {
        double distance = calculator.calculateDistanceKm(90.0, 0.0, -90.0, 0.0);
        assertEquals(20015.0, distance, 50.0);
    }

    @Test
    @DisplayName("Should handle International Date Line wrap-around (-180 to 180 degrees)")
    void testInternationalDateLine() {
        double distance = calculator.calculateDistanceKm(0.0, 179.0, 0.0, -179.0);
        // 2 degrees longitude difference at equator ~ 222 km
        assertEquals(222.3, distance, 5.0);
    }
}
