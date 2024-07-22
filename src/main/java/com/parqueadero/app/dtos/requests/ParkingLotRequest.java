package com.parqueadero.app.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotRequest {

    @NotBlank
    @Size(min = 10, max = 100)
    private String name;

    @NotNull
    @Min(value = 0)
    private Long pricePerHour;

    @NotNull
    @Min(value = 0)
    private Long capacity;
}
