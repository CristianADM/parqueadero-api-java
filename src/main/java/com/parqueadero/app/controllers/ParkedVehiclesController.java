package com.parqueadero.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleCountResponse;
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
    //Consultar los 10 vehiculos con mas registros
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/most-registration")
    public ResponseEntity<List<ParkedVehicleCountResponse>> findParkedVehiclesWithTheMostRegistration() {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesWithTheMostRegistration());
    }

    //Consultar los 10 vehiculos con mas registros por parqueadero
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/most-registration-by-parking-lot/{parkingLotId}")
    public ResponseEntity<List<ParkedVehicleCountResponse>> findParkedVehiclesWithTheMostRegistrationByParkingLot(@PathVariable Long parkingLotId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesWithTheMostRegistrationByParkingLot(parkingLotId));
    }
    
    //Consultar los vehiculos que son primera vez en el parqueadero
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/first-time/{parkingLotId}")
    public ResponseEntity<List<ParkedVehicleResponse>> findParkedVehiclesFirstTimeByParkingLot(@PathVariable Long parkingLotId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesFirstTimeByParkingLot(parkingLotId));
    }
    
    //Consultar las ganancias de un parqueadero por dia, semana, mes y año
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/earnings")
    public ResponseEntity<?> earningByTime(@RequestParam() String time) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesFirstTimeByParkingLot(parkingLotId));
    }

    //Consultar vehiculos por placa
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/search/{carPlate}")
    public ResponseEntity<List<ParkedVehicleResponse>> findParkedVehiclesByCarPlate(@PathVariable String carPlate) {
        return ResponseEntity.status(HttpStatus.OK).body(this.parkedVehiclesService.findParkedVehiclesByCarPlate(carPlate));
    }

    //Consultar vehiculos por parqueadero
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
