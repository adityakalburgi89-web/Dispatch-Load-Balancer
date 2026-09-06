package com.Aditya.Dispatch.Load.Balancer.mapper;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Order;
import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.OrderResponseDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toEntity(OrderRequestDto dto) {
        if (dto == null) return null;
        GeoLocation location = new GeoLocation(dto.getLatitude(), dto.getLongitude(), dto.getAddress());
        return new Order(dto.getOrderId(), location, dto.getPackageWeight(), dto.getPriority());
    }

    public OrderResponseDto toDto(Order entity) {
        if (entity == null) return null;
        return new OrderResponseDto(
                entity.getOrderId(),
                entity.getLocation() != null ? entity.getLocation().getLatitude() : null,
                entity.getLocation() != null ? entity.getLocation().getLongitude() : null,
                entity.getLocation() != null ? entity.getLocation().getAddress() : null,
                entity.getPackageWeight(),
                entity.getPriority(),
                entity.getStatus()
        );
    }
}
