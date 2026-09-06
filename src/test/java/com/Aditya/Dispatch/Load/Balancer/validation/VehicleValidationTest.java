package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class VehicleValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Valid VehicleRequestDto should pass validation")
    void testValidVehicleRequest() {
        VehicleRequestDto dto = new VehicleRequestDto("VEH001", 100.0, 37.7730, -122.4180, "Depot Central");
        Set<ConstraintViolation<VehicleRequestDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Blank vehicleId should fail validation")
    void testBlankVehicleId() {
        VehicleRequestDto dto = new VehicleRequestDto("", 100.0, 37.7730, -122.4180, "Depot Central");
        Set<ConstraintViolation<VehicleRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Negative capacity (< 0) should fail validation")
    void testNegativeCapacity() {
        VehicleRequestDto dto = new VehicleRequestDto("VEH001", -50.0, 37.7730, -122.4180, "Depot Central");
        Set<ConstraintViolation<VehicleRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("capacity")));
    }

    @Test
    @DisplayName("Invalid latitude should fail validation")
    void testInvalidLatitude() {
        VehicleRequestDto dto = new VehicleRequestDto("VEH001", 100.0, 91.0, -122.4180, "Depot Central");
        Set<ConstraintViolation<VehicleRequestDto>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }
}
