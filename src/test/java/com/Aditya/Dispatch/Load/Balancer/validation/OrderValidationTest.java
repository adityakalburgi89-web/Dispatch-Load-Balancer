package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrderValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid OrderRequestDto should pass bean validation")
    void testValidOrderRequest() {
        OrderRequestDto dto = new OrderRequestDto("ORD001", 37.7749, -122.4194, "Market St", 15.5, Priority.HIGH);
        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Blank orderId should fail validation")
    void testBlankOrderId() {
        OrderRequestDto dto = new OrderRequestDto("   ", 37.7749, -122.4194, "Market St", 15.5, Priority.HIGH);
        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("orderId")));
    }

    @Test
    @DisplayName("Latitude out of bounds (< -90 or > 90) should fail validation")
    void testInvalidLatitude() {
        OrderRequestDto dtoLow = new OrderRequestDto("ORD001", -95.0, -122.4194, "Market St", 15.5, Priority.HIGH);
        OrderRequestDto dtoHigh = new OrderRequestDto("ORD002", 95.0, -122.4194, "Market St", 15.5, Priority.HIGH);

        assertFalse(validator.validate(dtoLow).isEmpty());
        assertFalse(validator.validate(dtoHigh).isEmpty());
    }

    @Test
    @DisplayName("Longitude out of bounds (< -180 or > 180) should fail validation")
    void testInvalidLongitude() {
        OrderRequestDto dtoLow = new OrderRequestDto("ORD001", 37.7749, -185.0, "Market St", 15.5, Priority.HIGH);
        OrderRequestDto dtoHigh = new OrderRequestDto("ORD002", 37.7749, 185.0, "Market St", 15.5, Priority.HIGH);

        assertFalse(validator.validate(dtoLow).isEmpty());
        assertFalse(validator.validate(dtoHigh).isEmpty());
    }

    @Test
    @DisplayName("Negative packageWeight (< 0) should fail validation")
    void testNegativePackageWeight() {
        OrderRequestDto dto = new OrderRequestDto("ORD001", 37.7749, -122.4194, "Market St", -5.0, Priority.HIGH);
        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("packageWeight")));
    }

    @Test
    @DisplayName("Null priority should fail validation")
    void testNullPriority() {
        OrderRequestDto dto = new OrderRequestDto("ORD001", 37.7749, -122.4194, "Market St", 15.5, null);
        Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("priority")));
    }
}
