package com.Aditya.Dispatch.Load.Balancer.solver;

import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponse;
import com.Aditya.Dispatch.Load.Balancer.model.entity.OrderEntity;
import com.Aditya.Dispatch.Load.Balancer.model.entity.VehicleEntity;

import java.util.List;

public interface DispatchSolverStrategy {

    /**
     * Solves the dispatch load balancing problem and returns a structured dispatch plan.
     *
     * @param orders List of pending delivery orders
     * @param vehicles List of available fleet vehicles
     * @return DispatchPlanResponse containing route allocations, metrics, and unassigned orders
     */
    DispatchPlanResponse solve(List<OrderEntity> orders, List<VehicleEntity> vehicles);
}
