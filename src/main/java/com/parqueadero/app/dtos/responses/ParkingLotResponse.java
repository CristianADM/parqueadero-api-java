package com.parqueadero.app.dtos.responses;

import com.parqueadero.app.models.ParkingLotEntity;

import lombok.Data;

@Data
public class ParkingLotResponse {
    
    private Long id;

    private String name;

    private Long pricePerHour;

    private Long capacity;

    private UserResponse user;

    public ParkingLotResponse(ParkingLotEntity parkingLotEntity) {
        this.id = parkingLotEntity.getId();
        this.name = parkingLotEntity.getName();
        this.pricePerHour = parkingLotEntity.getPricePerHour();
        this.capacity = parkingLotEntity.getCapacity();
        this.user = new UserResponse(parkingLotEntity.getUser());
    }
}