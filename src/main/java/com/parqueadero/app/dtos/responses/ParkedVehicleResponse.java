package com.parqueadero.app.dtos.responses;

import java.time.LocalDateTime;

import com.parqueadero.app.models.ParkedVehiclesEntity;

public class ParkedVehicleResponse {

    private Long id;

    private ParkingLotResponse parkingLot;

    private String carPlate;

    private LocalDateTime arrivalDate;
    
    private LocalDateTime departureDate;

    private Long timeValue;

    public ParkedVehicleResponse() {
    }

    public ParkedVehicleResponse(ParkedVehiclesEntity parkedVehiclesEntity) {
        this.id = parkedVehiclesEntity.getId();
        this.parkingLot = new ParkingLotResponse(parkedVehiclesEntity.getParkingLotEntity());
        this.carPlate = parkedVehiclesEntity.getCarPlate();
        this.arrivalDate = parkedVehiclesEntity.getAudit().getCreateAt();
        this.departureDate = parkedVehiclesEntity.getDepartureDate();
        this.timeValue = parkedVehiclesEntity.getTimeValue();
    }
}