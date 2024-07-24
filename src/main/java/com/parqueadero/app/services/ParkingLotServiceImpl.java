package com.parqueadero.app.services;

import org.springframework.stereotype.Service;

import com.parqueadero.app.dtos.requests.ParkingLotRequest;
import com.parqueadero.app.dtos.responses.ParkingLotResponse;
import com.parqueadero.app.exceptions.BadRequestException;
import com.parqueadero.app.repositories.ParkingLotRepository;
import com.parqueadero.app.services.interfaces.IParkingLotService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ParkingLotServiceImpl implements IParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingLotResponse createParkingLot(ParkingLotRequest parkingLotRequest) {
        return ParkingLotResponse.builder().build();
    }

    @Override
    public void validUpdateNameParkingLot(String value) {
        if(parkingLotRepository.findByName(value).isPresent()) {
            throw new BadRequestException("name", "The name already exist");
        }
    }
}
