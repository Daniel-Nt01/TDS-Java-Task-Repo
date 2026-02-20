package com.dnt.tds_java_task.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dnt.tds_java_task.customexceptions.DuplicateCarException;
import com.dnt.tds_java_task.customexceptions.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.customexceptions.VehicleNotFoundException;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;
import com.dnt.tds_java_task.service.CarParkService;

@RestController
public class ParkingController {
    @Autowired
    private CarParkService carParkService;

    @PutMapping("/reset")
    public ResponseEntity<?> resetCarPark() {
        carParkService.resetCarPark();
        return ResponseEntity.ok("Car Park reset to empty and default size");
    }

    @PutMapping("/reset/{numberOfSpaces}")
    public ResponseEntity<?> resetCarParkAssigningNumberOfSpacesByPassedInValue(
            @PathVariable("numberOfSpaces") int numberOfSpaces) throws RequiredValuesNotPassedInException {
        carParkService.resetCarPark(numberOfSpaces);
        return ResponseEntity.ok("Car Park reset to empty and number of parking spaces is set to : " + numberOfSpaces);
    }

    @GetMapping("/parking")
    public ResponseEntity<?> getNumberOfAvailableAndFullCarSpaces() {
        ParkingSpacesStatusResponse parkingSpacesStatusResponse = carParkService
                .getNumberOfAvailableAndOccupiedParkingSpaces();
        return ResponseEntity.ok(parkingSpacesStatusResponse);
    }

    @PostMapping("/parking")
    public ResponseEntity<?> parkPassedInVehicle(@RequestBody ParkedCar carToPark)
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        ParkingResponse parkingResponse = carParkService.parkNewCar(carToPark);
        return ResponseEntity.ok(parkingResponse);
    }

    @PostMapping("/parking/bill")
    public ResponseEntity<?> removeParkedVehicleAndBill(@RequestBody ParkedCar carToPark)
            throws VehicleNotFoundException, RequiredValuesNotPassedInException {
        ParkingResponse parkingResponse = carParkService.billCar(carToPark);
        return ResponseEntity.ok(parkingResponse);
    }

    @PostMapping("/park-car-in-past-by-number-of-minutes-from-now/{numberOfMinutesInThePast}")
    public ResponseEntity<?> parkPassedInVehicle(@RequestBody ParkedCar carToPark,
            @PathVariable("numberOfMinutesInThePast") int numberOfMinutesInThePast)
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        carToPark.setTimeIn(LocalDateTime.now().minusMinutes(numberOfMinutesInThePast));
        ParkingResponse parkingResponse = carParkService.parkNewCar(carToPark);
        return ResponseEntity.ok(parkingResponse);
    }
}
