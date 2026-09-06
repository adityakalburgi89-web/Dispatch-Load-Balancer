package com.Aditya.Dispatch.Load.Balancer.dto.response;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import java.util.ArrayList;
import java.util.List;

public class VehicleAssignmentDto {

    private String vehicleId;
    private Double capacity;
    private Double usedCapacity;
    private Double remainingCapacity;
    private LocationDto startLocation;
    private List<AssignedOrderStep> assignedOrders = new ArrayList<>();
    private Double totalRouteDistanceKm;

    public VehicleAssignmentDto() {
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

    public Double getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Double usedCapacity) {
        this.usedCapacity = usedCapacity;
    }

    public Double getRemainingCapacity() {
        return remainingCapacity;
    }

    public void setRemainingCapacity(Double remainingCapacity) {
        this.remainingCapacity = remainingCapacity;
    }

    public LocationDto getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LocationDto startLocation) {
        this.startLocation = startLocation;
    }

    public List<AssignedOrderStep> getAssignedOrders() {
        return assignedOrders;
    }

    public void setAssignedOrders(List<AssignedOrderStep> assignedOrders) {
        this.assignedOrders = assignedOrders;
    }

    public Double getTotalRouteDistanceKm() {
        return totalRouteDistanceKm;
    }

    public void setTotalRouteDistanceKm(Double totalRouteDistanceKm) {
        this.totalRouteDistanceKm = totalRouteDistanceKm;
    }

    public static class LocationDto {
        private Double latitude;
        private Double longitude;
        private String address;

        public LocationDto() {
        }

        public LocationDto(Double latitude, Double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
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
    }

    public static class AssignedOrderStep {
        private Integer sequenceOrder;
        private String orderId;
        private String address;
        private Double latitude;
        private Double longitude;
        private Double packageWeight;
        private Priority priority;
        private Double distanceFromPreviousKm;

        public AssignedOrderStep() {
        }

        public Integer getSequenceOrder() {
            return sequenceOrder;
        }

        public void setSequenceOrder(Integer sequenceOrder) {
            this.sequenceOrder = sequenceOrder;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public Double getDistanceFromPreviousKm() {
            return distanceFromPreviousKm;
        }

        public void setDistanceFromPreviousKm(Double distanceFromPreviousKm) {
            this.distanceFromPreviousKm = distanceFromPreviousKm;
        }
    }
}
