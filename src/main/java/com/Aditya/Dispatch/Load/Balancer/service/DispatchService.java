package com.Aditya.Dispatch.Load.Balancer.service;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.dto.response.DispatchPlanResponseDto;
import com.Aditya.Dispatch.Load.Balancer.repository.OrderRepository;
import com.Aditya.Dispatch.Load.Balancer.repository.VehicleRepository;
import com.Aditya.Dispatch.Load.Balancer.strategy.DispatchOptimizationStrategy;
import com.Aditya.Dispatch.Load.Balancer.validation.DispatchBusinessValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DispatchService {

    private final OrderRepository orderRepository;
    private final VehicleRepository vehicleRepository;
    private final DispatchOptimizationStrategy optimizationStrategy;
    private final DispatchBusinessValidator businessValidator;

    public DispatchService(OrderRepository orderRepository,
                           VehicleRepository vehicleRepository,
                           DispatchOptimizationStrategy optimizationStrategy,
                           DispatchBusinessValidator businessValidator) {
        this.orderRepository = orderRepository;
        this.vehicleRepository = vehicleRepository;
        this.optimizationStrategy = optimizationStrategy;
        this.businessValidator = businessValidator;
    }

    @Transactional(readOnly = true)
    public DispatchPlanResponseDto generateDispatchPlan() {
        List<Order> orders = orderRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();

        businessValidator.validateDispatchPrerequisites(orders, vehicles);

        return optimizationStrategy.solve(orders, vehicles);
    }
}
