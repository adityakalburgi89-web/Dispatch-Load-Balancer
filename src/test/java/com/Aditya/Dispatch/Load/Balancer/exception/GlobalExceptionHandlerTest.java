package com.Aditya.Dispatch.Load.Balancer.exception;

import com.Aditya.Dispatch.Load.Balancer.dto.response.ErrorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("OrderUnassignableException should return status=error and ORDER_UNASSIGNABLE code")
    void testHandleOrderUnassignableException() {
        OrderUnassignableException ex = new OrderUnassignableException(
                "Order ORD001 cannot be assigned because package weight 150 exceeds every vehicle capacity.");

        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleOrderUnassignable(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("ORDER_UNASSIGNABLE", response.getBody().getCode());
        assertEquals("Order ORD001 cannot be assigned because package weight 150 exceeds every vehicle capacity.", response.getBody().getMessage());
    }

    @Test
    @DisplayName("NoVehiclesAvailableException should return code=NO_VEHICLES_AVAILABLE")
    void testHandleNoVehiclesAvailableException() {
        NoVehiclesAvailableException ex = new NoVehiclesAvailableException("No vehicles available");
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleNoVehicles(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("NO_VEHICLES_AVAILABLE", response.getBody().getCode());
    }

    @Test
    @DisplayName("Generic Exception should mask internal details and return code=INTERNAL_SERVER_ERROR")
    void testGenericExceptionMasking() {
        Exception ex = new NullPointerException("Secret database connection password leaked!");
        ResponseEntity<ErrorResponseDto> response = exceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getCode());
        assertEquals("An unexpected internal server error occurred", response.getBody().getMessage());
        assertFalse(response.getBody().getMessage().contains("password"));
    }
}
