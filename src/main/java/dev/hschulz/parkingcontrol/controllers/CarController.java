package dev.hschulz.parkingcontrol.controllers;

import dev.hschulz.parkingcontrol.DTOS.CarDTO;
import dev.hschulz.parkingcontrol.models.Car;
import dev.hschulz.parkingcontrol.services.CarService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge=3600)
@RequestMapping("/car")
public class CarController {

    final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Object> saveCar(@RequestBody @Valid CarDTO carDTO){

        if(carService.existsByLicensePlateCar(carDTO.getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }

        var carModel = new Car();
        BeanUtils.copyProperties(carDTO,carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.save(carModel));
    }

    @GetMapping
    public ResponseEntity<Page<Car>> getAllCars(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(carService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneCar(@PathVariable(value = "id") Long id){
        Optional<Car> carOptional = carService.findById(id);
        if(carOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(carOptional.get());
    }
}
