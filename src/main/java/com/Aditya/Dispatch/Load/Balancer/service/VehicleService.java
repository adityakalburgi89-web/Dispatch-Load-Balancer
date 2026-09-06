package com.Aditya.Dispatch.Load.Balancer.service;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import com.Aditya.Dispatch.Load.Balancer.dto.request.VehicleRequestDto;
import com.Aditya.Dispatch.Load.Balancer.dto.response.VehicleResponseDto;
import com.Aditya.Dispatch.Load.Balancer.exception.ResourceNotFoundException;
import com.Aditya.Dispatch.Load.Balancer.mapper.VehicleMapper;
import com.Aditya.Dispatch.Load.Balancer.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Transactional
    public VehicleResponseDto createOrUpdateVehicle(VehicleRequestDto requestDto) {
        Optional<Vehicle> existingOpt = vehicleRepository.findByVehicleId(requestDto.getVehicleId());

        Vehicle vehicleToSave;
        if (existingOpt.isPresent()) {
            // Upsert / Update existing record cleanly
            vehicleToSave = existingOpt.get();
            vehicleToSave.setCapacity(requestDto.getCapacity());
            vehicleToSave.setCurrentLatitude(requestDto.getCurrentLatitude());
            vehicleToSave.setCurrentLongitude(requestDto.getCurrentLongitude());
            vehicleToSave.setCurrentAddress(requestDto.getCurrentAddress());
        } else {
            vehicleToSave = vehicleMapper.toEntity(requestDto);
        }

        Vehicle saved = vehicleRepository.save(vehicleToSave);
        return vehicleMapper.toDto(saved);
    }

    @Transactional
    public List<VehicleResponseDto> createOrUpdateVehiclesBatch(List<VehicleRequestDto> requestDtos) {
        return requestDtos.stream()
                .map(this::createOrUpdateVehicle)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VehicleResponseDto> getAllVehicles() {
        return vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VehicleResponseDto getVehicleById(String vehicleId) {
        Vehicle entity = vehicleRepository.findByVehicleId(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with vehicleId: " + vehicleId));
        return vehicleMapper.toDto(entity);
    }

    @Transactional
    public void deleteVehicle(String vehicleId) {
        if (!vehicleRepository.existsByVehicleId(vehicleId)) {
            throw new ResourceNotFoundException("Vehicle not found with vehicleId: " + vehicleId);
        }
        vehicleRepository.deleteByVehicleId(vehicleId);
    }

    @Transactional
    public void deleteAllVehicles() {
        vehicleRepository.deleteAll();
    }
}
