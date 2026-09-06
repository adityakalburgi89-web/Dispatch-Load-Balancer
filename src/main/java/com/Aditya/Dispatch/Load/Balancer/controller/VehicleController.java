package com.Aditya.Dispatch.Load.Balancer.controller;

import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.VehicleResponseDto;
import com.Aditya.Dispatch.Load.Balancer.service.VehicleService;
import com.Aditya.Dispatch.Load.Balancer.validation.BatchValidator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final BatchValidator batchValidator;

    public VehicleController(VehicleService vehicleService, BatchValidator batchValidator) {
        this.vehicleService = vehicleService;
        this.batchValidator = batchValidator;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(@Valid @RequestBody VehicleRequestDto requestDto) {
        VehicleResponseDto response = vehicleService.createOrUpdateVehicle(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<VehicleResponseDto>> createVehiclesBatch(@RequestBody List<VehicleRequestDto> requestDtos) {
        batchValidator.validateVehicleBatch(requestDtos);
        List<VehicleResponseDto> responses = vehicleService.createOrUpdateVehiclesBatch(requestDtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(@PathVariable String vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable String vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllVehicles() {
        vehicleService.deleteAllVehicles();
        return ResponseEntity.noContent().build();
    }
}
