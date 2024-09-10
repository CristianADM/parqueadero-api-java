package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.models.ParkedVehiclesEntity;
import java.util.List;


@Repository
public interface ParkedVehiclesRepository extends JpaRepository<ParkedVehiclesEntity, Long> {

    @Query(value = "SELECT pv FROM ParkedVehiclesEntity pv " +
        "JOIN pv.parkingLotEntity pl " +
        "WHERE " + 
        "(pv.carPlate LIKE %:carPlate%) " + 
        "AND (:idUser IS NULL OR pl.user.id = :idUser)")
    List<ParkedVehiclesEntity> findByCarPlateAndUserId(String carPlate, Long idUser);

    List<ParkedVehiclesEntity> findByParkingLotEntityId(Long parkingLotId);

    boolean existsByCarPlateAndDepartureDateIsNull(String carPlate);
    
    Optional<ParkedVehiclesEntity> findByCarPlateAndDepartureDateIsNull(String carPlate);

    @Query(value = "SELECT COUNT(pv) FROM ParkedVehiclesEntity pv " + 
        "WHERE pv.parkingLotEntity.id = :idParkingLot " +
        "AND pv.departureDate IS NULL ")
    Long countParkedVehiclesByParkingLotId(Long idParkingLot);
}
