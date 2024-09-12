package com.parqueadero.app.dtos.responses;

import lombok.Data;

@Data
public class ParkedVehicleCountResponse {

    private String carPlate;
    
    private Long numberTimesRegistered;

    public ParkedVehicleCountResponse() {
    }

    public ParkedVehicleCountResponse(String carPlate, Long count) {
        this.carPlate = carPlate;
        this.numberTimesRegistered = count;
    }
}