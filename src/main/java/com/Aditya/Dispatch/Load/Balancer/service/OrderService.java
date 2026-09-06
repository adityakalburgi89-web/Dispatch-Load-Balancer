package com.Aditya.Dispatch.Load.Balancer.service;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.OrderResponseDto;
import com.Aditya.Dispatch.Load.Balancer.exception.ResourceNotFoundException;
import com.Aditya.Dispatch.Load.Balancer.mapper.OrderMapper;
import com.Aditya.Dispatch.Load.Balancer.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public OrderResponseDto createOrUpdateOrder(OrderRequestDto requestDto) {
        Optional<Order> existingOpt = orderRepository.findByOrderId(requestDto.getOrderId());

        Order orderToSave;
        if (existingOpt.isPresent()) {
            // Upsert / Update existing record cleanly
            orderToSave = existingOpt.get();
            orderToSave.setLatitude(requestDto.getLatitude());
            orderToSave.setLongitude(requestDto.getLongitude());
            orderToSave.setAddress(requestDto.getAddress());
            orderToSave.setPackageWeight(requestDto.getPackageWeight());
            orderToSave.setPriority(requestDto.getPriority());
        } else {
            orderToSave = orderMapper.toEntity(requestDto);
        }

        Order saved = orderRepository.save(orderToSave);
        return orderMapper.toDto(saved);
    }

    @Transactional
    public List<OrderResponseDto> createOrUpdateOrdersBatch(List<OrderRequestDto> requestDtos) {
        return requestDtos.stream()
                .map(this::createOrUpdateOrder)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(String orderId) {
        Order entity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with orderId: " + orderId));
        return orderMapper.toDto(entity);
    }

    @Transactional
    public void deleteOrder(String orderId) {
        if (!orderRepository.existsByOrderId(orderId)) {
            throw new ResourceNotFoundException("Order not found with orderId: " + orderId);
        }
        orderRepository.deleteByOrderId(orderId);
    }

    @Transactional
    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
