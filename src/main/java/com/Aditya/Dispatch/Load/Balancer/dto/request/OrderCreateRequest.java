package com.Aditya.Dispatch.Load.Balancer.dto.request;

import com.Aditya.Dispatch.Load.Balancer.model.enums.Priority;
import jakarta.validation.constraints.*;

public class OrderCreateRequest {

    @NotBlank(message = "orderId must not be blank")
    @Size(max = 64, message = "orderId must not exceed 64 characters")
    private String orderId;

    @NotNull(message = "latitude is required")
    @DecimalMin(value = "-90.0", message = "latitude must be >= -90.0 degrees")
    @DecimalMax(value = "90.0", message = "latitude must be <= 90.0 degrees")
    private Double latitude;

    @NotNull(message = "longitude is required")
    @DecimalMin(value = "-180.0", message = "longitude must be >= -180.0 degrees")
    @DecimalMax(value = "180.0", message = "longitude must be <= 180.0 degrees")
    private Double longitude;

    @NotBlank(message = "address must not be blank")
    @Size(max = 255, message = "address must not exceed 255 characters")
    private String address;

    @NotNull(message = "packageWeight is required")
    @Positive(message = "packageWeight must be strictly greater than 0")
    private Double packageWeight;

    @NotNull(message = "priority is required")
    private Priority priority;

    public OrderCreateRequest() {
    }

    public OrderCreateRequest(String orderId, Double latitude, Double longitude, String address, Double packageWeight, Priority priority) {
        this.orderId = orderId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.packageWeight = packageWeight;
        this.priority = priority;
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
}
