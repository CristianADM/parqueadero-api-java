package com.parqueadero.app.services.interfaces;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;

public interface IParkingLotService {

    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest);

    public void validUpdateNameParkingLot(String value);
}
