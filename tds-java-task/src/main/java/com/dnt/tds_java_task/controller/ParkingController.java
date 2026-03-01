package com.dnt.tds_java_task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dnt.tds_java_task.dto.request.CarToRemoveRequest;
import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.dto.response.ParkingResponse;
import com.dnt.tds_java_task.dto.response.ParkingSpacesStatusResponse;
import com.dnt.tds_java_task.service.ParkingService;

import jakarta.validation.Valid;

@RestController
public class ParkingController {
    
    private ParkingService parkingService;
    
    @Autowired
    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PutMapping("/reset")
    public ResponseEntity<String> resetCarPark() {
        parkingService.resetCarPark();
        return ResponseEntity.ok("Car Park reset to empty and default size");
    }

    @PutMapping("/reset/{numberOfSpaces}")
    public ResponseEntity<String> resetCarParkAssigningNumberOfSpacesByPassedInValue( @PathVariable("numberOfSpaces") int numberOfSpaces) {
        parkingService.resetCarParkWithNumberOfSpaces(numberOfSpaces);
        return ResponseEntity.ok("Car Park reset to empty and number of parking spaces is set to : " + numberOfSpaces);
    }

    @GetMapping("/parking")
    public ResponseEntity<ParkingSpacesStatusResponse> getNumberOfAvailableAndFullCarSpaces() {
        ParkingSpacesStatusResponse parkingSpacesStatusResponse = parkingService
                .getNumberOfAvailableAndOccupiedParkingSpaces();
        return ResponseEntity.ok(parkingSpacesStatusResponse);
    }

    @PostMapping("/parking")
    public ResponseEntity<ParkingResponse> parkPassedInVehicle(@Valid @RequestBody ParkedCarRequest carToPark) {
        ParkingResponse parkingResponse = parkingService.parkNewCar(carToPark);
        return ResponseEntity.ok(parkingResponse);
    }

    @PostMapping("/parking/bill")
    public ResponseEntity<ParkingResponse> removeParkedVehicleAndBill(@Valid @RequestBody CarToRemoveRequest carToRemove) {
        ParkingResponse parkingResponse = parkingService.billCar(carToRemove);
        return ResponseEntity.ok(parkingResponse);
    }
}
