package com.Aditya.Dispatch.Load.Balancer.domain.entity;

import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import com.Aditya.Dispatch.Load.Balancer.exception.InvalidCoordinateException;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles", indexes = {
    @Index(name = "idx_vehicles_vehicle_id", columnList = "vehicle_id", unique = true),
    @Index(name = "idx_vehicles_location", columnList = "current_latitude, current_longitude")
})
public class Vehicle {

    private static final double MAX_VEHICLE_CAPACITY_KG = 10_000_000.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "vehicle_id", nullable = false, unique = true, length = 64)
    private String vehicleId;

    @Column(name = "capacity", nullable = false)
    private Double capacity;

    @Column(name = "current_latitude", nullable = false)
    private Double currentLatitude;

    @Column(name = "current_longitude", nullable = false)
    private Double currentLongitude;

    @Column(name = "current_address", nullable = false, length = 255)
    private String currentAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Vehicle() {
    }

    public Vehicle(String vehicleId, Double capacity, Double currentLatitude, Double currentLongitude, String currentAddress) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.currentAddress = currentAddress;
    }

    public Vehicle(String vehicleId, Double capacity, GeoLocation location) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        if (location != null) {
            this.currentLatitude = location.getLatitude();
            this.currentLongitude = location.getLongitude();
            this.currentAddress = location.getAddress();
        }
    }

    @PrePersist
    @PreUpdate
    public void validateAndSanitize() {
        this.updatedAt = LocalDateTime.now();

        if (vehicleId == null || vehicleId.trim().isEmpty()) {
            throw new IllegalArgumentException("vehicleId cannot be null or blank");
        }

        if (currentLatitude == null || currentLatitude < -90.0 || currentLatitude > 90.0) {
            throw new InvalidCoordinateException("Current latitude must be between -90.0 and 90.0 degrees");
        }

        if (currentLongitude == null || currentLongitude < -180.0 || currentLongitude > 180.0) {
            throw new InvalidCoordinateException("Current longitude must be between -180.0 and 180.0 degrees");
        }

        if (capacity == null || capacity <= 0.0) {
            throw new IllegalArgumentException("Vehicle capacity must be strictly greater than 0");
        }

        if (capacity > MAX_VEHICLE_CAPACITY_KG || Double.isInfinite(capacity) || Double.isNaN(capacity)) {
            throw new IllegalArgumentException("Vehicle capacity exceeds maximum allowed threshold of " + MAX_VEHICLE_CAPACITY_KG + " kg");
        }
    }

    public GeoLocation getCurrentLocation() {
        if (currentLatitude == null || currentLongitude == null) {
            return null;
        }
        return new GeoLocation(currentLatitude, currentLongitude, currentAddress);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
