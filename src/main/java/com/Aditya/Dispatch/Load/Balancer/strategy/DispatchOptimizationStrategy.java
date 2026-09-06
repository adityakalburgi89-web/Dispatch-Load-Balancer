package com.Aditya.Dispatch.Load.Balancer.strategy;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;

import java.util.List;

public interface DispatchOptimizationStrategy {

    /**
     * Optimizes the assignment of delivery orders to vehicles.
     *
     * @param orders List of pending orders
     * @param vehicles List of registered fleet vehicles
     * @return Formatted dispatch plan response
     */
    DispatchPlanResponseDto solve(List<Order> orders, List<Vehicle> vehicles);
}
