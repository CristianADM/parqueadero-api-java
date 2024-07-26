package com.parqueadero.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.services.interfaces.IParkingLotService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RequestMapping("/parking-lot")
@RestController
public class ParkingLotController {

    private IParkingLotService parkingLotService;

    public ParkingLotController(IParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingLotResponse>> getMethodName() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkingLotService.findAllParkingLots());
    }
    
    @PostMapping
    public ResponseEntity<ParkingLotResponse> createParkingLot(@Valid @RequestBody ParkingLotRequest parkingLot) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.parkingLotService.createParkingLot(parkingLot));
    }
}
