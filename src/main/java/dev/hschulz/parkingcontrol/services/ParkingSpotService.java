package dev.hschulz.parkingcontrol.services;

import dev.hschulz.parkingcontrol.models.ParkingSpot;
import dev.hschulz.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

// Em 55:00 fala sobre criar uma interface desse service e usar para regras de negócios
// https://www.youtube.com/watch?v=LXRU-Z36GEU

@Service
public class ParkingSpotService {

    final ParkingSpotRepository parkingSpotRepository;

    final
    CarService carService;

    public ParkingSpotService(ParkingSpotRepository parkingSpotRepository, CarService carService) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.carService = carService;
    }



    @Transactional
    public ParkingSpot save(ParkingSpot parkingSpot) {
        return parkingSpotRepository.save(parkingSpot);
    }

    public Page<ParkingSpot> findAll(Pageable pageable) {
        return parkingSpotRepository.findAll(pageable);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return carService.existsByLicensePlateCar(licensePlateCar);
    }

    public boolean existsByParkingSpotNumber(String parkingSpotNumber) {
        return parkingSpotRepository.existsByParkingSpotNumber(parkingSpotNumber);
    }

    public boolean existsByApartmentAndBlock(String apartment, String block) {
        return parkingSpotRepository.existsByApartmentAndBlock(apartment,block);
    }

    public Optional<ParkingSpot> findById(Long id) {
        return parkingSpotRepository.findById(id);
    }

    @Transactional
    public void delete(ParkingSpot parkingSpot) {
        parkingSpotRepository.delete(parkingSpot);
    }

}
