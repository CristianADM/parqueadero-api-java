package com.parqueadero.app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.exceptions.NotFoundException;
import com.parqueadero.app.models.Audit;
import com.parqueadero.app.models.ParkingLotEntity;
import com.parqueadero.app.models.UserEntity;
import com.parqueadero.app.repositories.ParkingLotRepository;
import com.parqueadero.app.services.interfaces.IParkingLotService;
import com.parqueadero.app.services.interfaces.IUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ParkingLotServiceImpl implements IParkingLotService {
    
    private final ParkingLotRepository parkingLotRepository;
    private final IUserService userService;

    @Override
    public List<ParkingLotResponse> findAllParkingLots() {
        List<ParkingLotEntity> parkingLots = this.parkingLotRepository.findAll();

        List<ParkingLotResponse> parkingLotResponses = new ArrayList<>();

        parkingLots.forEach(parking -> {
            parkingLotResponses.add(new ParkingLotResponse(parking));
        });
        
        return parkingLotResponses;
    }
    
    @Transactional
    @Override
    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {
        this.validUpdateNameParkingLot(parkingLotRequest.getName());

        UserEntity user = this.userService.findUserByEmail(parkingLotRequest.getEmailUser());

        ParkingLotEntity parkingLotEntity = ParkingLotEntity.builder()
            .name(parkingLotRequest.getName())
            .pricePerHour(parkingLotRequest.getPricePerHour())
            .capacity(parkingLotRequest.getCapacity())
            .user(user)
            .audit(new Audit())
            .build();

        parkingLotEntity = this.parkingLotRepository.save(parkingLotEntity);

        return new ParkingLotResponse(parkingLotEntity);
    }

    @Transactional
    @Override
    public ParkingLotResponse updateParkingLot(Long idParkingLot, ParkingLotRequest parkingLotRequest) {
        ParkingLotEntity parkingLotEntity = this.findParkingLotById(idParkingLot);
        
        this.validUpdateNameParkingLot(parkingLotRequest.getName());

        UserEntity userEntity = this.userService.findUserByEmail(parkingLotRequest.getEmailUser());

        if(parkingLotRequest.getCapacity() < parkingLotEntity.getCapacity()) {
            //To Do
            //validar que hayan menos vehiculos actualmente en el parqueadero que la capacidad que viene en el request
            throw new BadRequestException("Capacity", "The capacity is less than the registered vehicles.");
        }

        parkingLotEntity.setName(parkingLotRequest.getName());
        parkingLotEntity.setPricePerHour(parkingLotRequest.getPricePerHour());
        parkingLotEntity.setCapacity(parkingLotRequest.getCapacity());
        parkingLotEntity.setUser(userEntity);

        parkingLotEntity = this.parkingLotRepository.save(parkingLotEntity);

        return new ParkingLotResponse(parkingLotEntity);
    }

    @Transactional
    @Override
    public ParkingLotResponse deleteParkingLot(Long idParkingLot) {
        ParkingLotEntity parkingLotEntity = this.findParkingLotById(idParkingLot);

        parkingLotEntity.getAudit().setActive(false);
        parkingLotEntity = this.parkingLotRepository.save(parkingLotEntity);

        return new ParkingLotResponse(parkingLotEntity);
    }

    @Override
    public void validUpdateNameParkingLot(String value) {
        if(parkingLotRepository.findByName(value).isPresent()) {
            throw new BadRequestException("name", "The name already exist");
        }
    }

    @Override
    public ParkingLotEntity findParkingLotById(Long idParkingLot) {
        return this.checkOptionalEmpty(this.parkingLotRepository.findById(idParkingLot));
    }

    @Override
    public ParkingLotEntity checkOptionalEmpty(Optional<ParkingLotEntity> parkingLotOptional) {
        if(parkingLotOptional.isEmpty()) {
            throw new NotFoundException("Id", "There is no user with that id");
        }
        return parkingLotOptional.get();
    }
}
