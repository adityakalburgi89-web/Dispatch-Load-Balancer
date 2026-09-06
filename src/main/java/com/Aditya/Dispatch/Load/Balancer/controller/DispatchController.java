package com.Aditya.Dispatch.Load.Balancer.controller;

import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.OrderResponseDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.VehicleResponseDto;
import com.Aditya.Dispatch.Load.Balancer.service.DispatchService;
import com.Aditya.Dispatch.Load.Balancer.service.OrderService;
import com.Aditya.Dispatch.Load.Balancer.service.VehicleService;
import com.Aditya.Dispatch.Load.Balancer.validation.BatchValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dispatch")
public class DispatchController {

    private final DispatchService dispatchService;
    private final OrderService orderService;
    private final VehicleService vehicleService;
    private final BatchValidator batchValidator;

    public DispatchController(DispatchService dispatchService,
                              OrderService orderService,
                              VehicleService vehicleService,
                              BatchValidator batchValidator) {
        this.dispatchService = dispatchService;
        this.orderService = orderService;
        this.vehicleService = vehicleService;
        this.batchValidator = batchValidator;
    }

    @PostMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> registerOrdersBatch(@RequestBody List<OrderRequestDto> requestDtos) {
        batchValidator.validateOrderBatch(requestDtos);
        List<OrderResponseDto> responses = orderService.createOrUpdateOrdersBatch(requestDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @PostMapping("/vehicles")
    public ResponseEntity<List<VehicleResponseDto>> registerVehiclesBatch(@RequestBody List<VehicleRequestDto> requestDtos) {
        batchValidator.validateVehicleBatch(requestDtos);
        List<VehicleResponseDto> responses = vehicleService.createOrUpdateVehiclesBatch(requestDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/plan")
    public ResponseEntity<DispatchPlanResponseDto> getDispatchPlan() {
        DispatchPlanResponseDto response = dispatchService.generateDispatchPlan();
        return ResponseEntity.ok(response);
    }
}
