package com.parqueadero.app.services.interfaces;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;

public interface IParkedVehiclesService {

    ParkedVehicleResponse registerVehicle(ParkedVehicleRequest parkedVehicleRequest);
    
    public void validIfExisteVehicleByCarPlate(String carPlate);

    Long countParkedVehiclesByParkingLotId(Long idParkingLot);
}
