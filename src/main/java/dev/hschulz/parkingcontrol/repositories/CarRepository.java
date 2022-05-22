package dev.hschulz.parkingcontrol.repositories;

import dev.hschulz.parkingcontrol.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByLicensePlateCar(String licensePlateCar);

}
