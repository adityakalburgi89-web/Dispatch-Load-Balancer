package com.Aditya.Dispatch.Load.Balancer.domain.entity;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.OrderStatus;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import com.Aditya.Dispatch.Load.Balancer.exception.InvalidCoordinateException;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_orders_order_id", columnList = "order_id", unique = true),
    @Index(name = "idx_orders_status_priority", columnList = "status, priority"),
    @Index(name = "idx_orders_location", columnList = "latitude, longitude")
})
public class Order {

    private static final double MAX_PACKAGE_WEIGHT_KG = 1_000_000.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true, length = 64)
    private String orderId;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "package_weight", nullable = false)
    private Double packageWeight;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 16)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "assigned_vehicle_id", length = 64)
    private String assignedVehicleId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Order() {
    }

    public Order(String orderId, Double latitude, Double longitude, String address, Double packageWeight, Priority priority) {
        this.orderId = orderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.packageWeight = packageWeight;
        this.priority = priority;
    }

    public Order(String orderId, GeoLocation location, Double packageWeight, Priority priority) {
        this.orderId = orderId;
        if (location != null) {
            this.latitude = location.getLatitude();
            this.longitude = location.getLongitude();
            this.address = location.getAddress();
        }
        this.packageWeight = packageWeight;
        this.priority = priority;
    }

    @PrePersist
    @PreUpdate
    public void validateAndSanitize() {
        this.updatedAt = LocalDateTime.now();

        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("orderId cannot be null or blank");
        }

        if (latitude == null || latitude < -90.0 || latitude > 90.0) {
            throw new InvalidCoordinateException("Latitude must be between -90.0 and 90.0 degrees");
        }

        if (longitude == null || longitude < -180.0 || longitude > 180.0) {
            throw new InvalidCoordinateException("Longitude must be between -180.0 and 180.0 degrees");
        }

        if (packageWeight == null || packageWeight <= 0.0) {
            throw new IllegalArgumentException("Package weight must be strictly greater than 0");
        }

        if (packageWeight > MAX_PACKAGE_WEIGHT_KG || Double.isInfinite(packageWeight) || Double.isNaN(packageWeight)) {
            throw new IllegalArgumentException("Package weight exceeds maximum allowed threshold of " + MAX_PACKAGE_WEIGHT_KG + " kg");
        }

        if (priority == null) {
            throw new IllegalArgumentException("Order priority cannot be null");
        }
    }

    public GeoLocation getLocation() {
        if (latitude == null || longitude == null) {
            return null;
        }
        return new GeoLocation(latitude, longitude, address);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getPackageWeight() {
        return packageWeight;
    }

    public void setPackageWeight(Double packageWeight) {
        this.packageWeight = packageWeight;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getAssignedVehicleId() {
        return assignedVehicleId;
    }

    public void setAssignedVehicleId(String assignedVehicleId) {
        this.assignedVehicleId = assignedVehicleId;
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
