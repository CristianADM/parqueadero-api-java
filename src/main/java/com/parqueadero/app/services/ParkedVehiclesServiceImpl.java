package com.parqueadero.app.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.exceptions.NotFoundException;
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
        ParkingLotEntity parkingLotEntity = this.parkingLotService
                .findParkingLotById(parkedVehicleRequest.getIdParkingLot());

        this.validIfExisteVehicleByCarPlate(parkedVehicleRequest.getCarPlate());

        if (parkingLotEntity.getCapacity() <= this.countParkedVehiclesByParkingLotId(parkingLotEntity.getId())) {
            throw new BadRequestException("capcity", "There are no spaces in the parking lot");
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
    public ParkedVehicleResponse registerDepartureVehicle(ParkedVehicleRequest parkedVehicleRequest) {
        ParkingLotEntity parkingLotEntity = this.parkingLotService
                .findParkingLotById(parkedVehicleRequest.getIdParkingLot());

        ParkedVehiclesEntity parkedVehiclesEntity = this.parkedVehiclesRepository
                .findByCarPlateAndDepartureDateIsNull(parkedVehicleRequest.getCarPlate())
                .orElseThrow(() -> new NotFoundException("carPlate", "A vehicle with that car plate was not found."));

        parkedVehiclesEntity.setDepartureDate(LocalDateTime.now().withNano(0).withSecond(0));
        parkedVehiclesEntity.setTimeValue(this.);

        parkedVehiclesEntity = this.parkedVehiclesRepository.save(parkedVehiclesEntity);
        return new ParkedVehicleResponse(parkedVehiclesEntity);
    }

    @Override
    public void validIfExisteVehicleByCarPlate(String carPlate) {
        this.parkedVehiclesRepository.findByCarPlateAndDepartureDateIsNull(carPlate.toUpperCase()).ifPresent(
                v -> {
                    throw new BadRequestException("carPlate", "There is a vehicle registered with that car plate.");
                });
    }

    @Override
    public Long countParkedVehiclesByParkingLotId(Long idParkingLot) {
        return this.parkedVehiclesRepository.countParkedVehiclesByParkingLotId(idParkingLot);
    }

    private Long calculateTimeValue
}