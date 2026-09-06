package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import com.Aditya.Dispatch.Load.Balancer.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DispatchBusinessValidatorTest {

    private DispatchBusinessValidator validator;

    @BeforeEach
    void setUp() {
        validator = new DispatchBusinessValidator();
    }

    @Test
    @DisplayName("Should throw NoOrdersAvailableException when order list is empty")
    void testNoOrdersAvailable() {
        List<Vehicle> vehicles = Collections.singletonList(new Vehicle("V1", 100.0, 37.77, -122.41, "Depot"));
        NoOrdersAvailableException ex = assertThrows(NoOrdersAvailableException.class,
                () -> validator.validateDispatchPrerequisites(Collections.emptyList(), vehicles));
        assertEquals("NO_ORDERS_AVAILABLE", ex.getCode());
    }

    @Test
    @DisplayName("Should throw NoVehiclesAvailableException when vehicle list is empty")
    void testNoVehiclesAvailable() {
        List<Order> orders = Collections.singletonList(new Order("ORD1", 37.77, -122.41, "Loc", 10.0, Priority.HIGH));
        NoVehiclesAvailableException ex = assertThrows(NoVehiclesAvailableException.class,
                () -> validator.validateDispatchPrerequisites(orders, Collections.emptyList()));
        assertEquals("NO_VEHICLES_AVAILABLE", ex.getCode());
    }

    @Test
    @DisplayName("Should throw OrderUnassignableException when an order exceeds max single vehicle capacity")
    void testOrderExceedsMaxVehicleCapacity() {
        Order order = new Order("ORD001", 37.77, -122.41, "Loc", 150.0, Priority.HIGH);
        Vehicle vehicle = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");

        OrderUnassignableException ex = assertThrows(OrderUnassignableException.class,
                () -> validator.validateDispatchPrerequisites(Collections.singletonList(order), Collections.singletonList(vehicle)));

        assertEquals("ORDER_UNASSIGNABLE", ex.getCode());
        assertTrue(ex.getMessage().contains("ORD001"));
        assertTrue(ex.getMessage().contains("150.0"));
    }

    @Test
    @DisplayName("Should throw InsufficientFleetCapacityException when total order weight exceeds total fleet capacity")
    void testTotalWeightExceedsTotalFleetCapacity() {
        Order o1 = new Order("ORD1", 37.77, -122.41, "Loc", 60.0, Priority.HIGH);
        Order o2 = new Order("ORD2", 37.78, -122.42, "Loc", 60.0, Priority.MEDIUM);

        Vehicle v1 = new Vehicle("V1", 100.0, 37.77, -122.41, "Depot");

        InsufficientFleetCapacityException ex = assertThrows(InsufficientFleetCapacityException.class,
                () -> validator.validateDispatchPrerequisites(Arrays.asList(o1, o2), Collections.singletonList(v1)));

        assertEquals("INSUFFICIENT_FLEET_CAPACITY", ex.getCode());
        assertTrue(ex.getMessage().contains("120.0"));
    }
}
