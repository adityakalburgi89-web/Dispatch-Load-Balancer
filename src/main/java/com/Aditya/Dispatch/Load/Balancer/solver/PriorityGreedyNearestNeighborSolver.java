package com.Aditya.Dispatch.Load.Balancer.solver;

import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponse;
import com.Aditya.Dispatch.Load.Balancer.model.entity.OrderEntity;
import com.Aditya.Dispatch.Load.Balancer.model.entity.VehicleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriorityGreedyNearestNeighborSolver implements DispatchSolverStrategy {

    @Override
    public DispatchPlanResponse solve(List<OrderEntity> orders, List<VehicleEntity> vehicles) {
        return new DispatchPlanResponse();
    }
}
