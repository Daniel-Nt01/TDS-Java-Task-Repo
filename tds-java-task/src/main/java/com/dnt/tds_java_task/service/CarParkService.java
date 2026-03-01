package com.dnt.tds_java_task.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dnt.tds_java_task.dto.request.CarToRemoveRequest;
import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.dto.response.ParkingResponse;
import com.dnt.tds_java_task.dto.response.ParkingSpacesStatusResponse;
import com.dnt.tds_java_task.exception.DuplicateCarException;
import com.dnt.tds_java_task.exception.VehicleNotFoundException;
import com.dnt.tds_java_task.repository.CarPark;
import com.dnt.tds_java_task.repository.CarParkRepo;

@Component
public class CarParkService implements ParkingService {
    private CarPark carPark;

    public CarParkService() {
    }

    @Autowired
    public CarParkService(CarPark carPark) {
        this.carPark = carPark;
    }

    @Override
    public int getNumberOfAvailableCarParkSpaces() {
        return carPark.getNumberOfAvailableCarParkSpaces();
    }

    @Override
    public void resetCarPark() {
        carPark = new CarParkRepo();
    }

    @Override
    public void resetCarParkWithNumberOfSpaces(int numberOfSpaces) {
        if (numberOfSpaces > 0 && numberOfSpaces < 100) {
            carPark = new CarParkRepo(numberOfSpaces);
        }
        else {
            throw new IllegalArgumentException("Number Of Parking Spaces Must Be Between 1 and 99 inlusive");
        }

    }

    @Override
    public ParkingResponse parkNewCar(ParkedCarRequest carToPark) {
        makeSureCarIsNotAlreadyParked(carToPark.vehicleReg());
        int nextAvailableCarSpace = carPark.getNextAvailableCarSpace();
        if(Objects.isNull(carToPark.timeIn())) {
            carToPark = carToPark.withTimeIn(LocalDateTime.now());
        }
        
        carPark.addCarToCarPark(nextAvailableCarSpace, carToPark);

        return new ParkingResponse(carToPark.vehicleReg(), nextAvailableCarSpace + 1, carToPark.timeIn());
    }

    @Override
    public ParkingSpacesStatusResponse getNumberOfAvailableAndOccupiedParkingSpaces() {
        return new ParkingSpacesStatusResponse(getNumberOfAvailableCarParkSpaces(), getNumberOfOccupiedCarParkSpaces());
    }

    @Override
    public ParkingResponse billCar(CarToRemoveRequest carToRemove) {
        ParkedCarRequest removedParkedCar = carPark.removeCarFromCarPark(getCarParkSpotOfParkedCar(carToRemove.vehicleReg()));
        LocalDateTime timeOut = LocalDateTime.now();
        BigDecimal vehicleCharge = calculateAccumulatedChargeForParking(removedParkedCar, timeOut);
        String billId = generateBillId(removedParkedCar);
        
        return new ParkingResponse(billId, removedParkedCar.vehicleReg(), vehicleCharge,
                removedParkedCar.timeIn(), timeOut);
    }

    private int getNumberOfOccupiedCarParkSpaces() {
        return carPark.getTotalNumberOfCarParkSpaces() - carPark.getNumberOfAvailableCarParkSpaces();
    }

    private BigDecimal calculateAccumulatedChargeForParking(ParkedCarRequest carComingOut, LocalDateTime timeOut) {
        double chargeForParking = 0.00;
        long numberOfMinutesSpent = ChronoUnit.MINUTES.between(carComingOut.timeIn(), timeOut);
        if (numberOfMinutesSpent > 0) {
            int extraCharges = (int) (numberOfMinutesSpent / 5);
            double baseCharge = calculateBaseChargeForParking(carComingOut.vehicleType(), numberOfMinutesSpent);
            
            chargeForParking = baseCharge + extraCharges;
        }
             
        return new BigDecimal(String.valueOf(chargeForParking)).setScale(2);
    }

    private String generateBillId(ParkedCarRequest carComingOut) {
        return carComingOut.vehicleReg() + carComingOut.timeIn().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private void makeSureCarIsNotAlreadyParked(String vehicleReg) {
        if(carPark.getCarParkSpotOfParkedCar(vehicleReg) != -1) {
            throw new DuplicateCarException("A Car with this registration is already parked!");
        }
    }
    
    private int getCarParkSpotOfParkedCar(String carRegistration) {
        int carParkSpotOfParkedCar = carPark.getCarParkSpotOfParkedCar(carRegistration);
        if (carParkSpotOfParkedCar == -1) {
            throw new VehicleNotFoundException("No parked car has this registration");
        }
        return carParkSpotOfParkedCar;
    }
    
    private double calculateBaseChargeForParking(int vehicleType, long numberOfMinutesSpent) {
        return switch (vehicleType) {
            case 1:
                yield (numberOfMinutesSpent * 0.1);
            case 2:
                yield (numberOfMinutesSpent * 0.2);
            case 3:
                yield (numberOfMinutesSpent * 0.4);
            default:
                throw new IllegalArgumentException(
                        "No Price has been set for vehicle type: " + vehicleType);
        };
    }
    
}
