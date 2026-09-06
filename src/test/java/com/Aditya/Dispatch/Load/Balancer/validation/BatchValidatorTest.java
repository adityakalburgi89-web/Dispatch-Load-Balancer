package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.exception.DuplicateIdException;
import com.Aditya.Dispatch.Load.Balancer.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class BatchValidatorTest {

    private BatchValidator batchValidator;

    @BeforeEach
    void setUp() {
        batchValidator = new BatchValidator();
    }

    @Test
    @DisplayName("Should throw InvalidInputException for null or empty batch list")
    void testNullOrEmptyBatch() {
        assertThrows(InvalidInputException.class, () -> batchValidator.validateOrderBatch(null));
        assertThrows(InvalidInputException.class, () -> batchValidator.validateOrderBatch(Collections.emptyList()));
    }

    @Test
    @DisplayName("Should throw InvalidInputException when batch contains a null item")
    void testNullItemInBatch() {
        OrderRequestDto valid = new OrderRequestDto("ORD001", 37.7749, -122.4194, "Market St", 15.0, Priority.HIGH);
        assertThrows(InvalidInputException.class, () -> batchValidator.validateOrderBatch(Arrays.asList(valid, null)));
    }

    @Test
    @DisplayName("Should throw DuplicateIdException when duplicate orderId is present in same batch")
    void testDuplicateOrderIdInBatch() {
        OrderRequestDto order1 = new OrderRequestDto("ORD001", 37.7749, -122.4194, "Market St", 15.0, Priority.HIGH);
        OrderRequestDto order2 = new OrderRequestDto("ORD001", 37.7600, -122.4100, "Mission St", 20.0, Priority.MEDIUM);

        DuplicateIdException ex = assertThrows(DuplicateIdException.class,
                () -> batchValidator.validateOrderBatch(Arrays.asList(order1, order2)));
        assertTrue(ex.getMessage().contains("ORD001"));
    }

    @Test
    @DisplayName("Should throw DuplicateIdException when duplicate vehicleId is present in same batch")
    void testDuplicateVehicleIdInBatch() {
        VehicleRequestDto v1 = new VehicleRequestDto("VEH001", 100.0, 37.7730, -122.4180, "Depot 1");
        VehicleRequestDto v2 = new VehicleRequestDto("VEH001", 50.0, 37.7800, -122.4000, "Depot 2");

        DuplicateIdException ex = assertThrows(DuplicateIdException.class,
                () -> batchValidator.validateVehicleBatch(Arrays.asList(v1, v2)));
        assertTrue(ex.getMessage().contains("VEH001"));
    }
}
