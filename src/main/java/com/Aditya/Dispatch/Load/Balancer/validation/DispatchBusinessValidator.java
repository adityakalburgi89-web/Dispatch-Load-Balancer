package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.exception.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DispatchBusinessValidator {

    public void validateDispatchPrerequisites(List<Order> orders, List<Vehicle> vehicles) {
        if (orders == null || orders.isEmpty()) {
            throw new NoOrdersAvailableException("No orders available in the system for dispatch execution");
        }

        if (vehicles == null || vehicles.isEmpty()) {
            throw new NoVehiclesAvailableException("No vehicles available in the system for dispatch execution");
        }

        double maxFleetCapacity = vehicles.stream()
                .mapToDouble(Vehicle::getCapacity)
                .max()
                .orElse(0.0);

        for (Order order : orders) {
            if (order.getPackageWeight() != null && order.getPackageWeight() > maxFleetCapacity) {
                throw new OrderUnassignableException("Order " + order.getOrderId() +
                        " cannot be assigned because package weight " + order.getPackageWeight() +
                        " exceeds every vehicle capacity.");
            }
        }

        double totalOrderWeight = orders.stream()
                .mapToDouble(o -> o.getPackageWeight() != null ? o.getPackageWeight() : 0.0)
                .sum();

        double totalFleetCapacity = vehicles.stream()
                .mapToDouble(Vehicle::getCapacity)
                .sum();

        if (totalOrderWeight > totalFleetCapacity) {
            throw new InsufficientFleetCapacityException("Total package weight (" + totalOrderWeight +
                    " kg) exceeds total available fleet capacity (" + totalFleetCapacity + " kg)");
        }
    }
}
