package dev.hschulz.parkingcontrol.services;

import dev.hschulz.parkingcontrol.models.ParkingSpotModel;
import dev.hschulz.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

// Em 55:00 fala sobre criar uma interface desse service e usar para regras de negócios
// https://www.youtube.com/watch?v=LXRU-Z36GEU

@Service
public class ParkingSpotService {

    final ParkingSpotRepository parkingSpotRepository;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
    }

    @Transactional
    public ParkingSpotModel save(ParkingSpotModel parkingSpotModel) {
        return parkingSpotRepository.save(parkingSpotModel);
    }
}