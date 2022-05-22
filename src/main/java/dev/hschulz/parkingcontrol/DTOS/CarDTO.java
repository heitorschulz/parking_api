package dev.hschulz.parkingcontrol.DTOS;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CarDTO {


    @NotBlank
    @Size(max = 7)
    private String licensePlateCar;

    @NotBlank
    @Size(max = 70)
    private String brandCar;

    @NotBlank
    @Size(max = 70)
    private String modelCar;

    @NotBlank
    @Size(max = 70)
    private String colorCar;

}
