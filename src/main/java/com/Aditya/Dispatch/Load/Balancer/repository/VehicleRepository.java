package com.Aditya.Dispatch.Load.Balancer.repository;

import com.Aditya.Dispatch.Load.Balancer.domain.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByVehicleId(String vehicleId);

    boolean existsByVehicleId(String vehicleId);

    void deleteByVehicleId(String vehicleId);
}
