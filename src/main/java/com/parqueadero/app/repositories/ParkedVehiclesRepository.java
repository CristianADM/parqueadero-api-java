package com.parqueadero.app.repositories;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.parqueadero.app.dtos.responses.ParkedVehicleCountResponse;
import com.parqueadero.app.models.ParkedVehiclesEntity;

import java.util.ArrayList;
import java.util.List;


@Repository
public interface ParkedVehiclesRepository extends JpaRepository<ParkedVehiclesEntity, Long> {

    @Query("SELECT pv.carPlate, COUNT(pv) as count " +
       "FROM ParkedVehiclesEntity pv " +
       "WHERE (:parkingLotId IS NULL OR pv.parkingLotEntity.id = :parkingLotId) " +
       "GROUP BY pv.carPlate " +
       "ORDER BY count DESC")
    List<Object[]> findTop10VehiculosConMasRegistros(Long parkingLotId, Pageable pageable);

    default List<ParkedVehicleCountResponse> findParkedVehiclesWithTheMostRegistration(Long parkingLotId) {
        List<ParkedVehicleCountResponse> response = new ArrayList<>();

        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> resultados = this.findTop10VehiculosConMasRegistros(parkingLotId, pageable);

        resultados.forEach(result -> {
            response.add(new ParkedVehicleCountResponse((String) result[0], (Long) result[1]));
        });

        return response;
    }

    @Query(value = "SELECT pv " +
        "FROM ParkedVehiclesEntity pv " +
        "WHERE (:parkingLotId IS NULL OR pv.parkingLotEntity.id = :parkingLotId) " +
        "GROUP BY pv.id " +
       "HAVING COUNT(pv.id) = 1 ")
    List<ParkedVehiclesEntity> findFirstTimeByParkingLotId(Long parkingLotId);

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
