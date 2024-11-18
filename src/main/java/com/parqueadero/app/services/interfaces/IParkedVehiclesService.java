package com.parqueadero.app.services.interfaces;

import java.util.List;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleCountResponse;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;

public interface IParkedVehiclesService {

    List<ParkedVehicleCountResponse> findParkedVehiclesWithTheMostRegistration();
    
    List<ParkedVehicleCountResponse> findParkedVehiclesWithTheMostRegistrationByParkingLot(Long parkingLotId);
    
    List<ParkedVehicleResponse> findParkedVehiclesFirstTimeByParkingLot(Long parkingLotId);
    
    Long earningsByTimeAndParkingId(Long parkingLotId, String time);

    List<ParkedVehicleResponse> findParkedVehiclesByCarPlate(String carPlate);

    List<ParkedVehicleResponse> findParkedVehiclesByParkingLot(Long parkingLotId);

    ParkedVehicleResponse registerVehicle(ParkedVehicleRequest parkedVehicleRequest);

    ParkedVehicleResponse registerDepartureVehicle(ParkedVehicleRequest parkedVehicleRequest);
    
    public void validIfExisteVehicleByCarPlate(String carPlate);

    Long countParkedVehiclesByParkingLotId(Long idParkingLot);
}