package com.Aditya.Dispatch.Load.Balancer.dto.response;

import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.UnassignedReason;
import java.util.ArrayList;
import java.util.List;

public class DispatchPlanResponseDto {

    private SummaryDto summary;
    private List<VehicleAssignmentDto> dispatchPlan = new ArrayList<>();
    private List<UnassignedOrderDto> unassignedOrders = new ArrayList<>();

    public DispatchPlanResponseDto() {
    }

    public SummaryDto getSummary() {
        return summary;
    }

    public void setSummary(SummaryDto summary) {
        this.summary = summary;
    }

    public List<VehicleAssignmentDto> getDispatchPlan() {
        return dispatchPlan;
    }

    public void setDispatchPlan(List<VehicleAssignmentDto> dispatchPlan) {
        this.dispatchPlan = dispatchPlan;
    }

    public List<UnassignedOrderDto> getUnassignedOrders() {
        return unassignedOrders;
    }

    public void setUnassignedOrders(List<UnassignedOrderDto> unassignedOrders) {
        this.unassignedOrders = unassignedOrders;
    }

    public static class SummaryDto {
        private int totalOrders;
        private int assignedOrdersCount;
        private int unassignedOrdersCount;
        private double totalDistanceKm;
        private double totalWeightDeliveredKg;
        private double fleetCapacityUtilizationPercentage;
        private long executionTimeMs;

        public SummaryDto() {
        }

        public int getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(int totalOrders) {
            this.totalOrders = totalOrders;
        }

        public int getAssignedOrdersCount() {
            return assignedOrdersCount;
        }

        public void setAssignedOrdersCount(int assignedOrdersCount) {
            this.assignedOrdersCount = assignedOrdersCount;
        }

        public int getUnassignedOrdersCount() {
            return unassignedOrdersCount;
        }

        public void setUnassignedOrdersCount(int unassignedOrdersCount) {
            this.unassignedOrdersCount = unassignedOrdersCount;
        }

        public double getTotalDistanceKm() {
            return totalDistanceKm;
        }

        public void setTotalDistanceKm(double totalDistanceKm) {
            this.totalDistanceKm = totalDistanceKm;
        }

        public double getTotalWeightDeliveredKg() {
            return totalWeightDeliveredKg;
        }

        public void setTotalWeightDeliveredKg(double totalWeightDeliveredKg) {
            this.totalWeightDeliveredKg = totalWeightDeliveredKg;
        }

        public double getFleetCapacityUtilizationPercentage() {
            return fleetCapacityUtilizationPercentage;
        }

        public void setFleetCapacityUtilizationPercentage(double fleetCapacityUtilizationPercentage) {
            this.fleetCapacityUtilizationPercentage = fleetCapacityUtilizationPercentage;
        }

        public long getExecutionTimeMs() {
            return executionTimeMs;
        }

        public void setExecutionTimeMs(long executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static class UnassignedOrderDto {
        private String orderId;
        private Double packageWeight;
        private Priority priority;
        private UnassignedReason reason;
        private String reasonDescription;

        public UnassignedOrderDto() {
        }

        public UnassignedOrderDto(String orderId, Double packageWeight, Priority priority, UnassignedReason reason) {
            this.orderId = orderId;
            this.packageWeight = packageWeight;
            this.priority = priority;
            this.reason = reason;
            this.reasonDescription = reason != null ? reason.getDescription() : null;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
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

        public UnassignedReason getReason() {
            return reason;
        }

        public void setReason(UnassignedReason reason) {
            this.reason = reason;
            if (reason != null) {
                this.reasonDescription = reason.getDescription();
            }
        }

        public String getReasonDescription() {
            return reasonDescription;
        }

        public void setReasonDescription(String reasonDescription) {
            this.reasonDescription = reasonDescription;
        }
    }
}
