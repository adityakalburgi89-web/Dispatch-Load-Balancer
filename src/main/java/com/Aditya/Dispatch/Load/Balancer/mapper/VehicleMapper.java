package com.Aditya.Dispatch.Load.Balancer.mapper;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.domain.valueobject.GeoLocation;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.VehicleResponseDto;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public Vehicle toEntity(VehicleRequestDto dto) {
        if (dto == null) return null;
        GeoLocation location = new GeoLocation(dto.getCurrentLatitude(), dto.getCurrentLongitude(), dto.getCurrentAddress());
        return new Vehicle(dto.getVehicleId(), dto.getCapacity(), location);
    }

    public VehicleResponseDto toDto(Vehicle entity) {
        if (entity == null) return null;
        return new VehicleResponseDto(
                entity.getVehicleId(),
                entity.getCapacity(),
                entity.getCurrentLocation() != null ? entity.getCurrentLocation().getLatitude() : null,
                entity.getCurrentLocation() != null ? entity.getCurrentLocation().getLongitude() : null,
                entity.getCurrentLocation() != null ? entity.getCurrentLocation().getAddress() : null
        );
    }
}
