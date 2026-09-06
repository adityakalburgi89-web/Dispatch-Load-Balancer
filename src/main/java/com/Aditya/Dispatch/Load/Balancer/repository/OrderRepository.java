package com.Aditya.Dispatch.Load.Balancer.repository;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.OrderStatus;
import com.Aditya.Dispatch.Load.Balancer.domain.enums.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByOrderId(String orderId);

    boolean existsByOrderId(String orderId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByPriority(Priority priority);

    void deleteByOrderId(String orderId);

    void deleteByStatus(OrderStatus status);
}
