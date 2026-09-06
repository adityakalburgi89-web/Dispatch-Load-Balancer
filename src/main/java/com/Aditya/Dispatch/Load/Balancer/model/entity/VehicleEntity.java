package com.Aditya.Dispatch.Load.Balancer.model.entity;

import com.Aditya.Dispatch.Load.Balancer.model.valueobject.GeoLocation;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {

    @Id
    @Column(name = "vehicle_id", nullable = false, length = 64)
    private String vehicleId;

    @Column(name = "capacity", nullable = false)
    private Double capacity;

    @Embedded
    private GeoLocation currentLocation;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public VehicleEntity() {
    }

    public VehicleEntity(String vehicleId, Double capacity, GeoLocation currentLocation) {
        this.vehicleId = vehicleId;
        this.capacity = capacity;
        this.currentLocation = currentLocation;
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

    public GeoLocation getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoLocation currentLocation) {
        this.currentLocation = currentLocation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
