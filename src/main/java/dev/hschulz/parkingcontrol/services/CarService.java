package dev.hschulz.parkingcontrol.services;

import dev.hschulz.parkingcontrol.models.Car;
import dev.hschulz.parkingcontrol.repositories.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {

    final
    CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Object save(Car car) {
        return carRepository.save(car);
    }

    public Page<Car> findAll(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public boolean existsByLicensePlateCar(String licensePlateCar) {
        return carRepository.existsByLicensePlateCar(licensePlateCar);
    }
}
