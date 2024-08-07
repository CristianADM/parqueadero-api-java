package com.parqueadero.app.dtos.requests;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkedVehicleRequest {

    @NotNull
    private Long idParkingLot;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "The carPlate must have 6 characters and be alphanumeric.")
    private String carPlate;
}
