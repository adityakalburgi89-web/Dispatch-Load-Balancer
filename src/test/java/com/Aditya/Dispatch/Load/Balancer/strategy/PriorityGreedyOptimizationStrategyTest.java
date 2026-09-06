package com.Aditya.Dispatch.Load.Balancer.strategy;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.UnassignedReason;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;
import com.Aditya.Dispatch.Load.Balancer.strategy.distance.HaversineDistanceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriorityGreedyOptimizationStrategyTest {

    private PriorityGreedyOptimizationStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PriorityGreedyOptimizationStrategy(new HaversineDistanceCalculator());
    }

    @Test
    @DisplayName("Should handle empty orders and empty vehicles gracefully")
    void testEmptyOrdersAndVehicles() {
        DispatchPlanResponseDto response = strategy.solve(Collections.emptyList(), Collections.emptyList());
        assertNotNull(response.getSummary());
        assertEquals(0, response.getSummary().getTotalOrders());

        Vehicle v = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");
        DispatchPlanResponseDto noOrdersResp = strategy.solve(Collections.emptyList(), Collections.singletonList(v));
        assertEquals(0, noOrdersResp.getSummary().getTotalOrders());

        Order o = new Order("O1", 37.77, -122.41, "Loc", 10.0, Priority.HIGH);
        DispatchPlanResponseDto noVehiclesResp = strategy.solve(Collections.singletonList(o), Collections.emptyList());
        assertEquals(1, noVehiclesResp.getSummary().getUnassignedOrdersCount());
        assertEquals(UnassignedReason.NO_VEHICLES_AVAILABLE, noVehiclesResp.getUnassignedOrders().get(0).getReason());
    }

    @Test
    @DisplayName("Should process HIGH priority orders before MEDIUM and LOW priority orders")
    void testPriorityHierarchyPrecedence() {
        Vehicle vehicle = new Vehicle("V1", 50.0, 37.77, -122.41, "Depot");

        Order low = new Order("ORD-LOW", 37.771, -122.411, "Near", 30.0, Priority.LOW);
        Order med = new Order("ORD-MED", 37.780, -122.420, "Mid", 30.0, Priority.MEDIUM);
        Order high = new Order("ORD-HIGH", 37.800, -122.450, "Far", 30.0, Priority.HIGH);

        DispatchPlanResponseDto response = strategy.solve(Arrays.asList(low, med, high), Collections.singletonList(vehicle));

        assertEquals(1, response.getSummary().getAssignedOrdersCount());
        assertEquals(2, response.getSummary().getUnassignedOrdersCount());
        assertEquals("ORD-HIGH", response.getDispatchPlan().get(0).getAssignedOrders().get(0).getOrderId());
    }

    @Test
    @DisplayName("Should handle exact capacity match (Order weight == Vehicle capacity)")
    void testExactCapacityMatch() {
        Vehicle vehicle = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");
        Order order = new Order("ORD-EXACT", 37.78, -122.42, "Loc", 100.0, Priority.HIGH);

        DispatchPlanResponseDto response = strategy.solve(Collections.singletonList(order), Collections.singletonList(vehicle));

        assertEquals(1, response.getSummary().getAssignedOrdersCount());
        assertEquals(0.0, response.getDispatchPlan().get(0).getRemainingCapacity(), 1e-4);
    }

    @Test
    @DisplayName("Should mark order as EXCEEDS_MAX_SINGLE_VEHICLE_CAPACITY when order > all vehicles")
    void testOrderLargerThanAllVehicles() {
        Vehicle vehicle = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");
        Order huge = new Order("ORD-HUGE", 37.78, -122.42, "Loc", 250.0, Priority.HIGH);

        DispatchPlanResponseDto response = strategy.solve(Collections.singletonList(huge), Collections.singletonList(vehicle));

        assertEquals(0, response.getSummary().getAssignedOrdersCount());
        assertEquals(UnassignedReason.EXCEEDS_MAX_SINGLE_VEHICLE_CAPACITY, response.getUnassignedOrders().get(0).getReason());
    }

    @Test
    @DisplayName("Should handle zero-capacity vehicles by skipping them")
    void testZeroCapacityVehicle() {
        Vehicle zeroCap = new Vehicle("V1", 0.0, 37.77, -122.41, "Depot");
        Order order = new Order("ORD1", 37.78, -122.42, "Loc", 10.0, Priority.HIGH);

        DispatchPlanResponseDto response = strategy.solve(Collections.singletonList(order), Collections.singletonList(zeroCap));

        assertEquals(0, response.getSummary().getAssignedOrdersCount());
        assertEquals(1, response.getSummary().getUnassignedOrdersCount());
    }

    @Test
    @DisplayName("Should recover from greedy dead-end scenario using backtracking swap")
    void testGreedyDeadEndRecovery() {
        Vehicle vLarge = new Vehicle("V-LARGE", 100.0, 37.77, -122.41, "Depot 1");
        Vehicle vSmall = new Vehicle("V-SMALL", 30.0, 37.78, -122.42, "Depot 2");

        Order smallOrder = new Order("ORD-SMALL", 37.771, -122.411, "Loc 1", 20.0, Priority.HIGH);
        Order largeOrder = new Order("ORD-LARGE", 37.772, -122.412, "Loc 2", 90.0, Priority.HIGH);

        DispatchPlanResponseDto response = strategy.solve(Arrays.asList(smallOrder, largeOrder), Arrays.asList(vLarge, vSmall));

        assertEquals(2, response.getSummary().getAssignedOrdersCount());
        assertEquals(0, response.getSummary().getUnassignedOrdersCount());
    }

    @Test
    @DisplayName("Should guarantee deterministic output for identical inputs")
    void testDeterministicOutput() {
        Vehicle v1 = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");
        Vehicle v2 = new Vehicle("V2", 100.0, 37.77, -122.41, "Depot");

        Order o1 = new Order("ORD1", 37.78, -122.42, "Loc", 50.0, Priority.HIGH);
        Order o2 = new Order("ORD2", 37.79, -122.43, "Loc", 50.0, Priority.HIGH);

        List<Order> orders = Arrays.asList(o1, o2);
        List<Vehicle> vehicles = Arrays.asList(v1, v2);

        DispatchPlanResponseDto run1 = strategy.solve(orders, vehicles);
        DispatchPlanResponseDto run2 = strategy.solve(orders, vehicles);

        assertEquals(run1.getSummary().getAssignedOrdersCount(), run2.getSummary().getAssignedOrdersCount());
        assertEquals(run1.getSummary().getTotalDistanceKm(), run2.getSummary().getTotalDistanceKm());
    }
}
