package com.parqueadero.app.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.models.ParkingLotEntity;

public interface IParkingLotService {

    List<ParkingLotResponse> findAllParkingLots();
    
    ParkingLotEntity findParkingLotById(Long idParkingLot);

    ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest);
    
    ParkingLotResponse updateParkingLot(Long idParkingLot, ParkingLotRequest parkingLotRequest);
    
    ParkingLotResponse deleteParkingLot(Long idParkingLot);

    void validUpdateNameParkingLot(String value);

    ParkingLotEntity checkOptionalEmpty(Optional<ParkingLotEntity> parkingLotOptional);
}
