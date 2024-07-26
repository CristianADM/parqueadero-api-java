package com.parqueadero.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.dtos.responses.UserResponse;
import com.parqueadero.app.exceptions.BadRequestException;
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
            parkingLotResponses.add(ParkingLotResponse.builder()
                                    .name(parking.getName())
                                    .pricePerHour(parking.getPricePerHour())
                                    .capacity(parking.getCapacity())
                                    .user(UserResponse.builder()
                                            .email(parking.getUser().getEmail())
                                            .id(parking.getUser().getId())
                                            .isActive(parking.getUser().getAudit().isActive())
                                            .build())
                                    .build());
        });

        return parkingLotResponses;
    }

    @Transactional()
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

        return ParkingLotResponse.builder()
            .id(parkingLotEntity.getId())
            .name(parkingLotEntity.getName())
            .pricePerHour(parkingLotEntity.getPricePerHour())
            .capacity(parkingLotEntity.getCapacity())
            .user(UserResponse.builder()
                    .email(parkingLotEntity.getUser().getEmail())
                    .build())
            .build();
    }

    @Override
    public void validUpdateNameParkingLot(String value) {
        if(parkingLotRepository.findByName(value).isPresent()) {
            throw new BadRequestException("name", "The name already exist");
        }
    }
}
