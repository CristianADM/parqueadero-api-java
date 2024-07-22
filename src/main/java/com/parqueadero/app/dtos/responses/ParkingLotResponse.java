package com.parqueadero.app.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParkingLotResponse {
    
    private Long id;

    private String name;

    private Long pricePerHour;

    private Long capacity;

    private UserResponse user;
}
