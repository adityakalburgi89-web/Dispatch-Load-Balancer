package com.Aditya.Dispatch.Load.Balancer.strategy;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.UnassignedReason;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.VehicleAssignmentDto;
import com.Aditya.Dispatch.Load.Balancer.strategy.distance.DistanceCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PriorityGreedyOptimizationStrategy implements DispatchOptimizationStrategy {

    private static final double EPSILON = 1e-7;
    private final DistanceCalculator distanceCalculator;

    public PriorityGreedyOptimizationStrategy(DistanceCalculator distanceCalculator) {
        this.distanceCalculator = distanceCalculator;
    }

    @Override
    public DispatchPlanResponseDto solve(List<Order> orders, List<Vehicle> vehicles) {
        long startTime = System.currentTimeMillis();

        DispatchPlanResponseDto response = new DispatchPlanResponseDto();
        List<DispatchPlanResponseDto.UnassignedOrderDto> unassignedList = new ArrayList<>();

        if (orders == null || orders.isEmpty()) {
            DispatchPlanResponseDto.SummaryDto summary = new DispatchPlanResponseDto.SummaryDto();
            summary.setTotalOrders(0);
            summary.setAssignedOrdersCount(0);
            summary.setUnassignedOrdersCount(0);
            summary.setTotalDistanceKm(0.0);
            summary.setTotalWeightDeliveredKg(0.0);
            summary.setFleetCapacityUtilizationPercentage(0.0);
            summary.setExecutionTimeMs(System.currentTimeMillis() - startTime);
            response.setSummary(summary);
            return response;
        }

        if (vehicles == null || vehicles.isEmpty()) {
            for (Order order : orders) {
                unassignedList.add(new DispatchPlanResponseDto.UnassignedOrderDto(
                        order.getOrderId(), order.getPackageWeight(), order.getPriority(), UnassignedReason.NO_VEHICLES_AVAILABLE));
            }
            DispatchPlanResponseDto.SummaryDto summary = new DispatchPlanResponseDto.SummaryDto();
            summary.setTotalOrders(orders.size());
            summary.setAssignedOrdersCount(0);
            summary.setUnassignedOrdersCount(orders.size());
            summary.setTotalDistanceKm(0.0);
            summary.setTotalWeightDeliveredKg(0.0);
            summary.setFleetCapacityUtilizationPercentage(0.0);
            summary.setExecutionTimeMs(System.currentTimeMillis() - startTime);
            response.setSummary(summary);
            response.setUnassignedOrders(unassignedList);
            return response;
        }

        double maxSingleVehicleCapacity = vehicles.stream()
                .mapToDouble(v -> v.getCapacity() != null ? v.getCapacity() : 0.0)
                .max()
                .orElse(0.0);

        List<Order> validOrders = new ArrayList<>();

        for (Order order : orders) {
            if (order.getPackageWeight() == null || order.getPackageWeight() <= 0 ||
                order.getLatitude() == null || Math.abs(order.getLatitude()) > 90.0 ||
                order.getLongitude() == null || Math.abs(order.getLongitude()) > 180.0) {

                unassignedList.add(new DispatchPlanResponseDto.UnassignedOrderDto(
                        order.getOrderId(),
                        order.getPackageWeight() != null ? order.getPackageWeight() : 0.0,
                        order.getPriority() != null ? order.getPriority() : Priority.LOW,
                        UnassignedReason.INVALID_INPUT_DATA));
                continue;
            }

            if (order.getPackageWeight() > maxSingleVehicleCapacity + EPSILON) {
                unassignedList.add(new DispatchPlanResponseDto.UnassignedOrderDto(
                        order.getOrderId(), order.getPackageWeight(), order.getPriority(), UnassignedReason.EXCEEDS_MAX_SINGLE_VEHICLE_CAPACITY));
                continue;
            }

            validOrders.add(order);
        }

        Map<String, VehicleState> vehicleStates = new LinkedHashMap<>();
        for (Vehicle v : vehicles) {
            if (v.getCapacity() != null && v.getCapacity() > 0) {
                vehicleStates.put(v.getVehicleId(), new VehicleState(v));
            }
        }

        // Sort: Priority DESC, packageWeight DESC, orderId ASC
        validOrders.sort(Comparator.comparing(Order::getPriority, Comparator.comparingInt(Priority::getWeight).reversed())
                .thenComparing(Order::getPackageWeight, Comparator.reverseOrder())
                .thenComparing(Order::getOrderId));

        Map<Priority, List<Order>> priorityBuckets = validOrders.stream()
                .collect(Collectors.groupingBy(Order::getPriority, LinkedHashMap::new, Collectors.toList()));

        List<Priority> priorityOrder = Arrays.asList(Priority.HIGH, Priority.MEDIUM, Priority.LOW);

        for (Priority priority : priorityOrder) {
            List<Order> bucketOrders = priorityBuckets.getOrDefault(priority, Collections.emptyList());

            for (Order order : bucketOrders) {
                VehicleState bestVehicle = null;
                double bestScore = Double.MAX_VALUE;

                for (VehicleState vState : vehicleStates.values()) {
                    if (vState.remainingCapacity - order.getPackageWeight() >= -EPSILON) {
                        double dist = distanceCalculator.calculateDistanceKm(
                                vState.currentLat, vState.currentLon,
                                order.getLatitude(), order.getLongitude());

                        double capacityRatio = (vState.remainingCapacity - order.getPackageWeight()) / vState.vehicle.getCapacity();
                        double score = (0.85 * dist) + (0.15 * capacityRatio * 10.0);

                        if (score < bestScore - EPSILON) {
                            bestScore = score;
                            bestVehicle = vState;
                        } else if (Math.abs(score - bestScore) <= EPSILON && bestVehicle != null) {
                            if (vState.remainingCapacity < bestVehicle.remainingCapacity) {
                                bestVehicle = vState;
                            } else if (Math.abs(vState.remainingCapacity - bestVehicle.remainingCapacity) <= EPSILON &&
                                       vState.vehicle.getVehicleId().compareTo(bestVehicle.vehicle.getVehicleId()) < 0) {
                                bestVehicle = vState;
                            }
                        }
                    }
                }

                if (bestVehicle != null) {
                    bestVehicle.assignOrder(order, distanceCalculator);
                } else {
                    boolean swapped = attemptBacktrackingSwap(order, vehicleStates);
                    if (!swapped) {
                        unassignedList.add(new DispatchPlanResponseDto.UnassignedOrderDto(
                                order.getOrderId(), order.getPackageWeight(), order.getPriority(), UnassignedReason.FLEET_CAPACITY_EXHAUSTED));
                    }
                }
            }
        }

        List<VehicleAssignmentDto> assignmentDtos = new ArrayList<>();
        double totalFleetDistanceKm = 0.0;
        double totalWeightDeliveredKg = 0.0;
        double totalFleetCapacityKg = vehicles.stream().mapToDouble(v -> v.getCapacity() != null ? v.getCapacity() : 0.0).sum();
        int assignedCount = 0;

        for (VehicleState vState : vehicleStates.values()) {
            VehicleAssignmentDto dto = new VehicleAssignmentDto();
            dto.setVehicleId(vState.vehicle.getVehicleId());
            dto.setCapacity(round(vState.vehicle.getCapacity()));
            dto.setUsedCapacity(round(vState.usedCapacity));
            dto.setRemainingCapacity(round(Math.max(0.0, vState.remainingCapacity)));

            VehicleAssignmentDto.LocationDto startLoc = new VehicleAssignmentDto.LocationDto(
                    vState.vehicle.getCurrentLatitude(),
                    vState.vehicle.getCurrentLongitude(),
                    vState.vehicle.getCurrentAddress());
            dto.setStartLocation(startLoc);

            if (vState.assignedOrders.size() > 2) {
                vState.refineRouteWith2Opt(distanceCalculator);
            }

            List<VehicleAssignmentDto.AssignedOrderStep> steps = new ArrayList<>();
            double currentLat = vState.vehicle.getCurrentLatitude();
            double currentLon = vState.vehicle.getCurrentLongitude();
            double vehicleRouteDistance = 0.0;

            int seq = 1;
            for (Order order : vState.assignedOrders) {
                double legDist = distanceCalculator.calculateDistanceKm(
                        currentLat, currentLon,
                        order.getLatitude(), order.getLongitude());

                vehicleRouteDistance += legDist;
                currentLat = order.getLatitude();
                currentLon = order.getLongitude();

                VehicleAssignmentDto.AssignedOrderStep step = new VehicleAssignmentDto.AssignedOrderStep();
                step.setSequenceOrder(seq++);
                step.setOrderId(order.getOrderId());
                step.setAddress(order.getAddress());
                step.setLatitude(order.getLatitude());
                step.setLongitude(order.getLongitude());
                step.setPackageWeight(round(order.getPackageWeight()));
                step.setPriority(order.getPriority());
                step.setDistanceFromPreviousKm(round(legDist));

                steps.add(step);
            }

            dto.setAssignedOrders(steps);
            dto.setTotalRouteDistanceKm(round(vehicleRouteDistance));

            totalFleetDistanceKm += vehicleRouteDistance;
            totalWeightDeliveredKg += vState.usedCapacity;
            assignedCount += vState.assignedOrders.size();

            assignmentDtos.add(dto);
        }

        DispatchPlanResponseDto.SummaryDto summary = new DispatchPlanResponseDto.SummaryDto();
        summary.setTotalOrders(orders.size());
        summary.setAssignedOrdersCount(assignedCount);
        summary.setUnassignedOrdersCount(unassignedList.size());
        summary.setTotalDistanceKm(round(totalFleetDistanceKm));
        summary.setTotalWeightDeliveredKg(round(totalWeightDeliveredKg));
        double utilization = totalFleetCapacityKg > 0 ? (totalWeightDeliveredKg / totalFleetCapacityKg) * 100.0 : 0.0;
        summary.setFleetCapacityUtilizationPercentage(round(utilization));
        summary.setExecutionTimeMs(System.currentTimeMillis() - startTime);

        response.setSummary(summary);
        response.setDispatchPlan(assignmentDtos);
        response.setUnassignedOrders(unassignedList);

        return response;
    }

    private boolean attemptBacktrackingSwap(Order unassignedOrder, Map<String, VehicleState> vehicleStates) {
        for (VehicleState targetVehicle : vehicleStates.values()) {
            double neededCapacity = unassignedOrder.getPackageWeight() - targetVehicle.remainingCapacity;

            for (Order assignedOrder : new ArrayList<>(targetVehicle.assignedOrders)) {
                if (assignedOrder.getPriority() == unassignedOrder.getPriority() &&
                    assignedOrder.getPackageWeight() >= neededCapacity - EPSILON) {

                    for (VehicleState altVehicle : vehicleStates.values()) {
                        if (!altVehicle.vehicle.getVehicleId().equals(targetVehicle.vehicle.getVehicleId()) &&
                            altVehicle.remainingCapacity - assignedOrder.getPackageWeight() >= -EPSILON) {

                            targetVehicle.unassignOrder(assignedOrder, distanceCalculator);
                            altVehicle.assignOrder(assignedOrder, distanceCalculator);
                            targetVehicle.assignOrder(unassignedOrder, distanceCalculator);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static double round(double val) {
        return BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static class VehicleState {
        final Vehicle vehicle;
        double remainingCapacity;
        double usedCapacity;
        double currentLat;
        double currentLon;
        final List<Order> assignedOrders = new ArrayList<>();

        VehicleState(Vehicle vehicle) {
            this.vehicle = vehicle;
            this.remainingCapacity = vehicle.getCapacity();
            this.usedCapacity = 0.0;
            this.currentLat = vehicle.getCurrentLatitude();
            this.currentLon = vehicle.getCurrentLongitude();
        }

        void assignOrder(Order order, DistanceCalculator calculator) {
            assignedOrders.add(order);
            usedCapacity += order.getPackageWeight();
            remainingCapacity -= order.getPackageWeight();
            currentLat = order.getLatitude();
            currentLon = order.getLongitude();
        }

        void unassignOrder(Order order, DistanceCalculator calculator) {
            if (assignedOrders.remove(order)) {
                usedCapacity -= order.getPackageWeight();
                remainingCapacity += order.getPackageWeight();
                recomputeCurrentLocation();
            }
        }

        private void recomputeCurrentLocation() {
            if (assignedOrders.isEmpty()) {
                currentLat = vehicle.getCurrentLatitude();
                currentLon = vehicle.getCurrentLongitude();
            } else {
                Order last = assignedOrders.get(assignedOrders.size() - 1);
                currentLat = last.getLatitude();
                currentLon = last.getLongitude();
            }
        }

        void refineRouteWith2Opt(DistanceCalculator calculator) {
            boolean improved = true;
            while (improved) {
                improved = false;
                for (int i = 0; i < assignedOrders.size() - 1; i++) {
                    for (int k = i + 1; k < assignedOrders.size(); k++) {
                        double currentDistance = computeRouteDistance(assignedOrders, calculator);
                        Collections.reverse(assignedOrders.subList(i, k + 1));
                        double newDistance = computeRouteDistance(assignedOrders, calculator);

                        if (newDistance < currentDistance - EPSILON) {
                            improved = true;
                        } else {
                            Collections.reverse(assignedOrders.subList(i, k + 1));
                        }
                    }
                }
            }
            recomputeCurrentLocation();
        }

        private double computeRouteDistance(List<Order> route, DistanceCalculator calculator) {
            double dist = 0.0;
            double lat = vehicle.getCurrentLatitude();
            double lon = vehicle.getCurrentLongitude();

            for (Order o : route) {
                dist += calculator.calculateDistanceKm(lat, lon, o.getLatitude(), o.getLongitude());
                lat = o.getLatitude();
                lon = o.getLongitude();
            }
            return dist;
        }
    }
}
