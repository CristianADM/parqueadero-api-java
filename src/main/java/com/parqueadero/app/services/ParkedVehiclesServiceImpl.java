package com.parqueadero.app.services;

import org.springframework.stereotype.Service;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.models.Audit;
import com.parqueadero.app.models.ParkedVehiclesEntity;
import com.parqueadero.app.models.ParkingLotEntity;
import com.parqueadero.app.repositories.ParkedVehiclesRepository;
import com.parqueadero.app.services.interfaces.IParkedVehiclesService;
import com.parqueadero.app.services.interfaces.IParkingLotService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Service
public class ParkedVehiclesServiceImpl implements IParkedVehiclesService {

    private final ParkedVehiclesRepository parkedVehiclesRepository;
    private final IParkingLotService parkingLotService;

    @Override
    public ParkedVehicleResponse registerVehicle(ParkedVehicleRequest parkedVehicleRequest) {
        ParkingLotEntity parkingLotEntity = this.parkingLotService.findParkingLotById(parkedVehicleRequest.getIdParkingLot());

        this.validIfExisteVehicleByCarPlate(parkedVehicleRequest.getCarPlate());

        if(parkingLotEntity.getCapacity() < this.countParkedVehiclesByParkingLotId(parkingLotEntity.getId())) {
            //lanzar exception
        }

        ParkedVehiclesEntity parkedVehiclesEntity = ParkedVehiclesEntity.builder()
                                                        .parkingLotEntity(parkingLotEntity)
                                                        .carPlate(parkedVehicleRequest.getCarPlate().toUpperCase())
                                                        .audit(new Audit())
                                                        .build();

        parkedVehiclesEntity = this.parkedVehiclesRepository.save(parkedVehiclesEntity);
        return new ParkedVehicleResponse(parkedVehiclesEntity);
    }

    @Override
    public void validIfExisteVehicleByCarPlate(String carPlate) {
        this.parkedVehiclesRepository.findByCarPlateAndDepartureDateIsNull(carPlate.toUpperCase()).ifPresent(
            v -> {
                throw new BadRequestException("carPlate", "There is a vehicle registered with that car plate.");
            }
        );
    }

    @Override
    public Long countParkedVehiclesByParkingLotId(Long idParkingLot) {
        return this.parkedVehiclesRepository.countParkedVehiclesByParkingLotId(idParkingLot);
    }
}