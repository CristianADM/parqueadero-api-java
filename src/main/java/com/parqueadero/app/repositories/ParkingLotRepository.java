package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.parqueadero.app.models.ParkingLotEntity;

public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

    Optional<ParkingLotEntity> findByName(String name);
}
