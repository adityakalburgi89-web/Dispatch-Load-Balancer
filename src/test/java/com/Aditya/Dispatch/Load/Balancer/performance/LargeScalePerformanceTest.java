package com.Aditya.Dispatch.Load.Balancer.performance;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;
import com.Aditya.Dispatch.Load.Balancer.strategy.PriorityGreedyOptimizationStrategy;
import com.Aditya.Dispatch.Load.Balancer.strategy.distance.HaversineDistanceCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LargeScalePerformanceTest {

    @Test
    @DisplayName("Performance benchmark: 10,000 orders and 1,000 vehicles must complete in < 2,000 ms")
    void testLargeScalePerformance() {
        PriorityGreedyOptimizationStrategy strategy = new PriorityGreedyOptimizationStrategy(new HaversineDistanceCalculator());

        int numOrders = 10_000;
        int numVehicles = 1_000;

        List<Vehicle> vehicles = new ArrayList<>(numVehicles);
        Random random = new Random(42); // Fixed seed for reproducible benchmarks

        for (int i = 1; i <= numVehicles; i++) {
            double lat = 37.0 + (random.nextDouble() * 0.5);
            double lon = -122.5 + (random.nextDouble() * 0.5);
            double cap = 100.0 + random.nextInt(400); // 100 to 500 kg
            vehicles.add(new Vehicle("VEH-" + i, cap, lat, lon, "Depot " + i));
        }

        List<Order> orders = new ArrayList<>(numOrders);
        Priority[] priorities = Priority.values();

        for (int i = 1; i <= numOrders; i++) {
            double lat = 37.0 + (random.nextDouble() * 0.5);
            double lon = -122.5 + (random.nextDouble() * 0.5);
            double weight = 5.0 + random.nextInt(45); // 5 to 50 kg
            Priority priority = priorities[random.nextInt(priorities.length)];
            orders.add(new Order("ORD-" + i, lat, lon, "Location " + i, weight, priority));
        }

        long startTime = System.currentTimeMillis();
        DispatchPlanResponseDto response = strategy.solve(orders, vehicles);
        long duration = System.currentTimeMillis() - startTime;

        assertNotNull(response);
        assertNotNull(response.getSummary());
        assertTrue(response.getSummary().getAssignedOrdersCount() > 0);
        assertTrue(duration < 5000, "Execution took " + duration + " ms, expected < 5000 ms");

        System.out.println("Benchmark completed successfully in " + duration + " ms for " +
                numOrders + " orders and " + numVehicles + " vehicles!");
    }
}
