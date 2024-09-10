package com.parqueadero.app.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parqueadero.app.dtos.requests.ParkedVehicleRequest;
import com.parqueadero.app.dtos.responses.ParkedVehicleResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.exceptions.NotFoundException;
import com.parqueadero.app.exceptions.UnauthorizedException;
import com.parqueadero.app.models.Audit;
import com.parqueadero.app.models.ParkedVehiclesEntity;
import com.parqueadero.app.models.ParkingLotEntity;
import com.parqueadero.app.models.UserEntity;
import com.parqueadero.app.repositories.ParkedVehiclesRepository;
import com.parqueadero.app.services.interfaces.IParkedVehiclesService;
import com.parqueadero.app.services.interfaces.IParkingLotService;

@Service
public class ParkedVehiclesServiceImpl implements IParkedVehiclesService {

    private final ParkedVehiclesRepository parkedVehiclesRepository;
    private final IParkingLotService parkingLotService;

    public ParkedVehiclesServiceImpl(ParkedVehiclesRepository parkedVehiclesRepository,
            IParkingLotService parkingLotService) {
        this.parkedVehiclesRepository = parkedVehiclesRepository;
        this.parkingLotService = parkingLotService;
    }

    @Override
    public List<ParkedVehicleResponse> findParkedVehiclesByCarPlate(String carPlate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        
        if (loggedUser.isAdmin()) {
            return this.parkedVehiclesRepository.findByCarPlateAndUserId(carPlate.toUpperCase(), null)
            .stream()
            .map(ParkedVehicleResponse::new)
            .collect(Collectors.toList());
        } else {
            return this.parkedVehiclesRepository.findByCarPlateAndUserId(carPlate.toUpperCase(), loggedUser.getId())
            .stream()
            .map(ParkedVehicleResponse::new)
            .collect(Collectors.toList());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParkedVehicleResponse> findParkedVehiclesByParkingLot(Long parkingLotId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();

        ParkingLotEntity parkingLotEntity = this.parkingLotService.findParkingLotById(parkingLotId);
        
        if ((!parkingLotEntity.getUser().getId().equals(loggedUser.getId())) && (!loggedUser.isAdmin())) {
            throw new UnauthorizedException("User", "The parkinLot is not associated with the user");
        } 
        
        return this.parkedVehiclesRepository.findByParkingLotEntityId(parkingLotId)
        .stream()
        .map(ParkedVehicleResponse::new)  // Transforma cada `vehicle` en `ParkedVehicleResponse`
        .collect(Collectors.toList());
    }

    @Transactional
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

    @Transactional
    @Override
    public ParkedVehicleResponse registerDepartureVehicle(ParkedVehicleRequest parkedVehicleRequest) {
        ParkingLotEntity parkingLotEntity = this.parkingLotService
                .findParkingLotById(parkedVehicleRequest.getIdParkingLot());

        ParkedVehiclesEntity parkedVehiclesEntity = this.parkedVehiclesRepository
                .findByCarPlateAndDepartureDateIsNull(parkedVehicleRequest.getCarPlate())
                .orElseThrow(() -> new NotFoundException("carPlate", "A vehicle with that car plate was not found."));

        if(parkingLotEntity.getId() != parkedVehiclesEntity.getParkingLotEntity().getId()) {
            throw new NotFoundException("carPlate", "A vehicle with that car plate was not found.");
        }

        parkedVehiclesEntity.setDepartureDate(LocalDateTime.now().withNano(0).withSecond(0));
        parkedVehiclesEntity.setTimeValue(this.calculateTimeValue(parkedVehiclesEntity));

        parkedVehiclesEntity = this.parkedVehiclesRepository.save(parkedVehiclesEntity);
        return new ParkedVehicleResponse(parkedVehiclesEntity);
    }

    @Override
    public void validIfExisteVehicleByCarPlate(String carPlate) {
        if(this.parkedVehiclesRepository.existsByCarPlateAndDepartureDateIsNull(carPlate)) {
            throw new BadRequestException("carPlate", "There is a vehicle registered with that car plate.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Long countParkedVehiclesByParkingLotId(Long idParkingLot) {
        return this.parkedVehiclesRepository.countParkedVehiclesByParkingLotId(idParkingLot);
    }

    private Long calculateTimeValue(ParkedVehiclesEntity parkedVehiclesEntity) {

        Duration duration = Duration.between(parkedVehiclesEntity.getAudit().getCreateAt(), parkedVehiclesEntity.getDepartureDate());

        Long hours = duration.toHours();
        Long minutos = duration.toMinutes() % 60;

        Long resultado = (long)((minutos / 60.0) * parkedVehiclesEntity.getParkingLotEntity().getPricePerHour());

        return resultado + (hours * parkedVehiclesEntity.getParkingLotEntity().getPricePerHour());
    }
}