package com.Aditya.Dispatch.Load.Balancer.controller;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.repository.OrderRepository;
import com.Aditya.Dispatch.Load.Balancer.repository.VehicleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class DispatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        vehicleRepository.deleteAll();
    }

    @Test
    @DisplayName("Should successfully create vehicles, orders, and return dispatch plan endpoint")
    void testEndToEndDispatchPlanWorkflow() throws Exception {
        VehicleRequestDto vehicleReq = new VehicleRequestDto("VEH-01", 100.0, 37.7730, -122.4180, "Depot Central");
        mockMvc.perform(post("/api/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.vehicleId", is("VEH-01")))
                .andExpect(jsonPath("$.capacity", is(100.0)));

        OrderRequestDto order1 = new OrderRequestDto("ORD-01", 37.7749, -122.4194, "Market St", 30.0, Priority.HIGH);
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/dispatch/plan"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return HTTP 400 Bad Request when order input validation fails")
    void testOrderValidationFailure() throws Exception {
        OrderRequestDto invalidOrder = new OrderRequestDto("INVALID", 150.0, -122.4194, "Bad Lat", -10.0, Priority.HIGH);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrder)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("error")))
                .andExpect(jsonPath("$.code", is("VALIDATION_FAILED")))
                .andExpect(jsonPath("$.errors", hasSize(greaterThanOrEqualTo(2))));
    }
}
