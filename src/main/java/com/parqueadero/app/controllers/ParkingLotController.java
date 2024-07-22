package com.parqueadero.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.services.interfaces.IParkingLotService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping("/parking-lot")
@RestController
public class ParkingLotController {

    private IParkingLotService parkingLotService;

    public ParkingLotController(IParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    @GetMapping("path")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
    @PostMapping
    public ResponseEntity<ParkingLotResponse> createParkingLot(@RequestBody ParkingLotRequest parkingLot) {
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingLotService.createParkingLot(parkingLot));
    }
}
