package com.parqueadero.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;
import com.parqueadero.app.services.interfaces.IParkedVehiclesService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/parked-vehicles")
@RestController
public class ParkedVehiclesController {

    private final IParkedVehiclesService parkedVehiclesService;

    public ParkedVehiclesController(IParkedVehiclesService parkedVehiclesService) {
        this.parkedVehiclesService = parkedVehiclesService;
    }

    //---- GET ----\\
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{parkingLotId}")
    public ResponseEntity<List<ParkedVehicleResponse>> findParkedVehiclesByParkingLot(@PathVariable Long parkingLotId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesByParkingLot(parkingLotId));
    }

    //---- POST ----\\
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping
    public ResponseEntity<ParkedVehicleResponse> registerVehicle(@Valid @RequestBody ParkedVehicleRequest parkedVehicleRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkedVehiclesService.registerVehicle(parkedVehicleRequest));
    }

    //---- PATCH ----\\
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PatchMapping()
    public ResponseEntity<ParkedVehicleResponse> registerVehicleDeparture(@Valid @RequestBody ParkedVehicleRequest parkedVehicleRequest){
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkedVehiclesService.registerDepartureVehicle(parkedVehicleRequest));
    }

    //---- DELETE ----\\
}
