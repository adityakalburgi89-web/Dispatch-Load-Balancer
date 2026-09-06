package com.Aditya.Dispatch.Load.Balancer.dto.request;

import jakarta.validation.constraints.*;

public class VehicleRequestDto {

    @NotBlank(message = "vehicleId must not be blank")
    @Size(max = 64, message = "vehicleId must not exceed 64 characters")
    private String vehicleId;

    @NotNull(message = "capacity is required")
    @Positive(message = "capacity must be strictly greater than 0")
    private Double capacity;

    @NotNull(message = "currentLatitude is required")
    @DecimalMin(value = "-90.0", message = "currentLatitude must be >= -90.0 degrees")
    @DecimalMax(value = "90.0", message = "currentLatitude must be <= 90.0 degrees")
    private Double currentLatitude;

    @NotNull(message = "currentLongitude is required")
    @DecimalMin(value = "-180.0", message = "currentLongitude must be >= -180.0 degrees")
    @DecimalMax(value = "180.0", message = "currentLongitude must be <= 180.0 degrees")
    private Double currentLongitude;

    @NotBlank(message = "currentAddress must not be blank")
    @Size(max = 255, message = "currentAddress must not exceed 255 characters")
    private String currentAddress;

    public VehicleRequestDto() {
    }

    public VehicleRequestDto(String vehicleId, Double capacity, Double currentLatitude, Double currentLongitude, String currentAddress) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentAddress = currentAddress;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getCapacity() {
        return capacity;
    }

    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }
}
