package com.parqueadero.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.models.ParkedVehiclesEntity;

@Repository
public interface ParkedVehiclesRepository extends JpaRepository<ParkedVehiclesEntity, Long> {

}