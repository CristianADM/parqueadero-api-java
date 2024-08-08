package com.parqueadero.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.services.interfaces.IParkingLotService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor

@RequestMapping("/parking-lot")
@RestController
public class ParkingLotController {

    private final IParkingLotService parkingLotService;

    //---- GET ----\\
    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ParkingLotResponse>> findAllParkingLots() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingLotService.findAllParkingLots());
    }
    
    @GetMapping(value = "/{idParkingLot}")
    public ResponseEntity<ParkingLotResponse> getMethodName(@PathVariable Long idParkingLot) {
        return ResponseEntity.status(HttpStatus.OK).body(new ParkingLotResponse(this.parkingLotService.findParkingLotById(idParkingLot)));
    }
    
    //---- POST ----\\
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ParkingLotResponse> createParkingLot(@Valid @RequestBody ParkingLotRequest parkingLot) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkingLotService.createParkingLot(parkingLot));
    }


    //---- PUT ----\\
    @PutMapping("/{idParkingLot}")
    public ResponseEntity<ParkingLotResponse> updateParkingLot(@PathVariable Long idParkingLot, 
                                                                @Valid @RequestBody ParkingLotRequest parkingLotRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingLotService.updateParkingLot(idParkingLot, parkingLotRequest));
    }

    //---- DELETE ----\\
    @DeleteMapping("/{idParkingLot}")
    public ResponseEntity<ParkingLotResponse> deleteParkingLot(@PathVariable Long idParkingLot) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingLotService.deleteParkingLot(idParkingLot));
    }
}
