package com.Aditya.Dispatch.Load.Balancer.strategy.distance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HaversineDistanceCalculatorTest {

    private HaversineDistanceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new HaversineDistanceCalculator();
    }

    @Test
    @DisplayName("1. Same coordinates should yield 0 distance")
    void testSameCoordinates() {
        double distance = calculator.calculateDistanceKm(37.7749, -122.4194, 37.7749, -122.4194);
        assertEquals(0.0, distance, 1e-6);
    }

    @Test
    @DisplayName("2. Bangalore to Bangalore coordinates should yield approximately 0 distance")
    void testBangaloreToBangalore() {
        // Bangalore MG Road (12.9756, 77.6056) to Koramangala (12.9352, 77.6245) ~ 5 km
        double distanceSelf = calculator.calculateDistanceKm(12.9716, 77.5946, 12.9716, 77.5946);
        assertEquals(0.0, distanceSelf, 1e-6);

        double distanceShort = calculator.calculateDistanceKm(12.9756, 77.6056, 12.9352, 77.6245);
        assertTrue(distanceShort > 4.0 && distanceShort < 6.0);
    }

    @Test
    @DisplayName("3. Known coordinate pairs (San Francisco to New York ~ 4129 km)")
    void testKnownDistanceSFToNYC() {
        double distance = calculator.calculateDistanceKm(37.7749, -122.4194, 40.7128, -74.0060);
        assertEquals(4129.0, distance, 30.0);
    }

    @Test
    @DisplayName("4. Northern/Southern hemisphere cross-equator distance")
    void testNorthernSouthernHemisphere() {
        // London (51.5074, -0.1278) to Sydney (-33.8688, 151.2093) ~ 16990 km
        double distance = calculator.calculateDistanceKm(51.5074, -0.1278, -33.8688, 151.2093);
        assertEquals(16990.0, distance, 50.0);
    }

    @Test
    @DisplayName("5. Eastern/Western hemisphere cross-prime meridian distance")
    void testEasternWesternHemisphere() {
        // Greenwich UK (51.4826, 0.0) to NYC (40.7128, -74.0060) ~ 5570 km
        double distance = calculator.calculateDistanceKm(51.4826, 0.0, 40.7128, -74.0060);
        assertEquals(5570.0, distance, 50.0);
    }

    @Test
    @DisplayName("6. Coordinates near North/South poles")
    void testCoordinatesNearPoles() {
        double distance = calculator.calculateDistanceKm(89.9, 0.0, -89.9, 0.0);
        assertEquals(19992.0, distance, 50.0);
    }

    @Test
    @DisplayName("7. Coordinates crossing longitude 180 / -180 international date line")
    void testInternationalDateLineWrapAround() {
        // 179 E to 179 W across 180th meridian (2 deg longitudinal distance at equator ~ 222.4 km)
        double distance = calculator.calculateDistanceKm(0.0, 179.0, 0.0, -179.0);
        assertEquals(222.4, distance, 2.0);
    }

    @Test
    @DisplayName("8. Extreme boundary values (-90, 90, -180, 180)")
    void testBoundaryCoordinates() {
        double distLat = calculator.calculateDistanceKm(-90.0, -180.0, 90.0, 180.0);
        assertTrue(distLat > 0.0 && !Double.isNaN(distLat));
    }

    @Test
    @DisplayName("9. Floating-point tolerance and stability under micro-offsets")
    void testFloatingPointTolerance() {
        double distance = calculator.calculateDistanceKm(37.774900000001, -122.419400000001, 37.774900000002, -122.419400000002);
        assertTrue(distance < 0.001);
        assertFalse(Double.isNaN(distance));
    }
}
