package com.parqueadero.app.services.interfaces;

import java.util.List;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;

public interface IParkedVehiclesService {

    List<ParkedVehicleResponse> findParkedVehiclesByCarPlate(String carPlate);

    List<ParkedVehicleResponse> findParkedVehiclesByParkingLot(Long parkingLotId);

    ParkedVehicleResponse registerVehicle(ParkedVehicleRequest parkedVehicleRequest);

    ParkedVehicleResponse registerDepartureVehicle(ParkedVehicleRequest parkedVehicleRequest);
    
    public void validIfExisteVehicleByCarPlate(String carPlate);

    Long countParkedVehiclesByParkingLotId(Long idParkingLot);
}
