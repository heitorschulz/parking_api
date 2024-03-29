package dev.hschulz.parkingcontrol.controllers;

import dev.hschulz.parkingcontrol.DTOS.ParkingSpotDTO;
import dev.hschulz.parkingcontrol.models.Car;
import dev.hschulz.parkingcontrol.models.ParkingSpot;
import dev.hschulz.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDTO parkingSpotDTO){

        //Em 01:20:00 falou que seria interessante por essas validações em um custom validator
        if(parkingSpotService.existsByLicensePlateCar(parkingSpotDTO.getCar().getLicensePlateCar())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if(parkingSpotService.existsByParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if(parkingSpotService.existsByApartmentAndBlock(parkingSpotDTO.getApartment(),parkingSpotDTO.getBlock())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already registered for this apartment/block!");
        }


        var parkingSpotModel = new ParkingSpot(); //Para Java 11+
        //ParkinSpotModel parkingSpotModel = new ParkinSpotModel(); //para Java 8 ou <
        BeanUtils.copyProperties(parkingSpotDTO, parkingSpotModel);
        parkingSpotModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpot>> getAllParkingSpots(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") Long id){
        Optional<ParkingSpot> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot(@PathVariable(value = "id") Long id){
        Optional<ParkingSpot> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Parking Spot deleted successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id") Long id, @RequestBody @Valid ParkingSpotDTO parkingSpotDTO){
        Optional<ParkingSpot> parkingSpotModelOptional = parkingSpotService.findById(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }

        //Modo 1
/*        var parkingSpotModel = parkingSpotModelOptional.get();
        parkingSpotModel.setParkingSpotNumber(parkingSpotDTO.getParkingSpotNumber());
        parkingSpotModel.setLicensePlateCar(parkingSpotDTO.getLicensePlateCar());
        parkingSpotModel.setModelCar(parkingSpotDTO.getModelCar());
        parkingSpotModel.setBrandCar(parkingSpotDTO.getBrandCar());
        parkingSpotModel.setColorCar(parkingSpotDTO.getColorCar());
        parkingSpotModel.setResponsibleName(parkingSpotDTO.getResponsibleName());
        parkingSpotModel.setApartment(parkingSpotDTO.getApartment());
        parkingSpotModel.setBlock(parkingSpotDTO.getBlock());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));*/

        //Modo 2

        var parkingSpot = new ParkingSpot();
        BeanUtils.copyProperties(parkingSpotDTO,parkingSpot);

        var car = new Car();
        BeanUtils.copyProperties(parkingSpotDTO.getCar(),car);
        car.setId(parkingSpotModelOptional.get().getCar().getId());
        parkingSpot.setCar(car);

        parkingSpot.setId(parkingSpotModelOptional.get().getId());
        parkingSpot.setRegistrationDate(parkingSpotModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpot));

    }
}
