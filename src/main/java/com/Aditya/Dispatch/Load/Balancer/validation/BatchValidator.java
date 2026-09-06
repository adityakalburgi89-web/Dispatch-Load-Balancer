package com.Aditya.Dispatch.Load.Balancer.validation;

import com.Aditya.Dispatch.Load.Balancer.dto.request.OrderRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.exception.DuplicateIdException;
import com.Aditya.Dispatch.Load.Balancer.exception.InvalidInputException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BatchValidator {

    public void validateOrderBatch(List<OrderRequestDto> orders) {
        if (orders == null || orders.isEmpty()) {
            throw new InvalidInputException("Batch order request payload cannot be null or empty", "EMPTY_BATCH_REQUEST");
        }

        Set<String> seenIds = new HashSet<>();
        for (int i = 0; i < orders.size(); i++) {
            OrderRequestDto dto = orders.get(i);
            if (dto == null) {
                throw new InvalidInputException("Batch order request contains a null item at index " + i, "NULL_ITEM_IN_BATCH");
            }
            if (dto.getOrderId() != null && !seenIds.add(dto.getOrderId())) {
                throw new DuplicateIdException("Duplicate orderId '" + dto.getOrderId() + "' found within the same batch request payload", "DUPLICATE_BATCH_ORDER_ID");
            }
        }
    }

    public void validateVehicleBatch(List<VehicleRequestDto> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            throw new InvalidInputException("Batch vehicle request payload cannot be null or empty", "EMPTY_BATCH_REQUEST");
        }

        Set<String> seenIds = new HashSet<>();
        for (int i = 0; i < vehicles.size(); i++) {
            VehicleRequestDto dto = vehicles.get(i);
            if (dto == null) {
                throw new InvalidInputException("Batch vehicle request contains a null item at index " + i, "NULL_ITEM_IN_BATCH");
            }
            if (dto.getVehicleId() != null && !seenIds.add(dto.getVehicleId())) {
                throw new DuplicateIdException("Duplicate vehicleId '" + dto.getVehicleId() + "' found within the same batch request payload", "DUPLICATE_BATCH_VEHICLE_ID");
            }
        }
    }
}
