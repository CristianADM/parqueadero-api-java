package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.models.ParkedVehiclesEntity;

@Repository
public interface ParkedVehiclesRepository extends JpaRepository<ParkedVehiclesEntity, Long> {

    Optional<ParkedVehiclesEntity> findByCarPlateAndDepartureDateIsNull(String carPlate);

    Optional<ParkedVehiclesEntity> findByCarPlate(String carPlate);

    @Query(value = "SELECT COUNT(pv) FROM ParkedVehiclesEntity pv " + 
        "WHERE pv.parkingLotEntity.id = :idParkingLot " +
        "AND pv.departureDate IS NULL ")
    Long countParkedVehiclesByParkingLotId(Long idParkingLot);
}
