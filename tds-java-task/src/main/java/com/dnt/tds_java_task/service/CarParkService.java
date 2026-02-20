package com.dnt.tds_java_task.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dnt.tds_java_task.customexceptions.DuplicateCarException;
import com.dnt.tds_java_task.customexceptions.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.customexceptions.VehicleNotFoundException;
import com.dnt.tds_java_task.models.CarPark;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;

@Component
public class CarParkService {
    private CarPark carPark;

    public CarParkService() {
    }

    @Autowired
    public CarParkService(CarPark carPark) {
        this.carPark = carPark;
    }

    public int getNumberOfAvailableCarParkSpaces() {
        int available = 0;
        for (int key : carPark.getCarParkSpotMappedToParkedCar().keySet()) {
            if (carPark.getCarParkSpotMappedToParkedCar().get(key) == null) {
                available++;
            }
        }
        return available;
    }

    public void resetCarPark() {
        carPark = new CarPark();
    }

    public void resetCarPark(int numberOfSpaces) throws RequiredValuesNotPassedInException {
        if (numberOfSpaces > 0 && numberOfSpaces < 100) {
            carPark = new CarPark(numberOfSpaces);
        }
        else {
            throw new RequiredValuesNotPassedInException("Number Of Parking Spaces Must Be Between 1 and 99 inlusive");
        }

    }

    public ParkingResponse parkNewCar(ParkedCar carToPark)
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        if (carToPark != null && carToPark.getVehicleReg() != null && carToPark.getVehicleReg().length() >= 1
                && carToPark.getVehicleReg().length() <= 8
                && (carToPark.getVehicleType() >= 1 & carToPark.getVehicleType() <= 3)) {
            checkIfCarIsNotAlreadyParked(carToPark.getVehicleReg());
            int nextAvailableCarSpace = getNextAvailableCarSpace();
            ParkedCar parkedCar = null;
            if (carToPark.getTimeIn() != null) {
                parkedCar = new ParkedCar(carToPark.getVehicleReg(), carToPark.getVehicleType(), carToPark.getTimeIn());
            }
            else {
                parkedCar = new ParkedCar(carToPark.getVehicleReg(), carToPark.getVehicleType());
            }
            carPark.getCarParkSpotMappedToParkedCar().put(nextAvailableCarSpace, parkedCar);

            return new ParkingResponse(parkedCar.getVehicleReg(), nextAvailableCarSpace + 1, parkedCar.getTimeIn());
        }
        else {
            throw new RequiredValuesNotPassedInException(
                    "Car Registration must be included and non null and have a length between 1 and 8 characters inclusive and Vehicle Type must also be between 1 and 3 inclusive");
        }
    }

    public ParkingSpacesStatusResponse getNumberOfAvailableAndOccupiedParkingSpaces() {
        return new ParkingSpacesStatusResponse(getNumberOfAvailableCarParkSpaces(), getNumberOfOccupiedCarParkSpaces());
    }

    public ParkingResponse billCar(ParkedCar parkedCar)
            throws VehicleNotFoundException, RequiredValuesNotPassedInException {
        if (parkedCar.getVehicleReg() != null) {
            int parkingSpot = getParkingSpotThatPassedInCarIsParkedIn(parkedCar);
            ParkedCar foundParkedCar = carPark.getCarParkSpotMappedToParkedCar().put(parkingSpot, null);
            LocalDateTime timeOut = LocalDateTime.now();
            BigDecimal vehicleCharge = calculateAccumulatedChargeForParking(foundParkedCar, timeOut);
            String billId = generateBillId(foundParkedCar);
            return new ParkingResponse(billId, foundParkedCar.getVehicleReg(), vehicleCharge,
                    foundParkedCar.getTimeIn(), timeOut);
        }
        throw new RequiredValuesNotPassedInException("Car Registration must be included");
    }

    private int getNextAvailableCarSpace() throws NoAvailableCarSpaceException {
        for (int key : carPark.getCarParkSpotMappedToParkedCar().keySet()) {
            if (carPark.getCarParkSpotMappedToParkedCar().get(key) == null) {
                return key;
            }
        }
        throw new NoAvailableCarSpaceException("No Available Car Spaces");
    }

    private int getNumberOfOccupiedCarParkSpaces() {
        return carPark.getCarParkSpotMappedToParkedCar().keySet().size() - getNumberOfAvailableCarParkSpaces();
    }

    private int getParkingSpotThatPassedInCarIsParkedIn(ParkedCar parkedCar) throws VehicleNotFoundException {
        for (int key : carPark.getCarParkSpotMappedToParkedCar().keySet()) {
            ParkedCar carParkedInCurrentSpot = carPark.getCarParkSpotMappedToParkedCar().get(key);
            if (carParkedInCurrentSpot != null
                    && carParkedInCurrentSpot.getVehicleReg().equals(parkedCar.getVehicleReg())) {
                return key;
            }
        }
        throw new VehicleNotFoundException("No parked car has this registration");
    }

    private long getNumberOfMinutesBetweenCarBeingParkedAndItBeingBilled(ParkedCar carComingOut,
            LocalDateTime timeOut) {
        return ChronoUnit.MINUTES.between(carComingOut.getTimeIn(), timeOut);
    }

    private BigDecimal calculateAccumulatedChargeForParking(ParkedCar carComingOut, LocalDateTime timeOut) {
        double chargeForParking = 0.00;
        long numberOfMinutesSpent = getNumberOfMinutesBetweenCarBeingParkedAndItBeingBilled(carComingOut, timeOut);
        if (numberOfMinutesSpent > 0) {
            int extraCharges = (int) (numberOfMinutesSpent / 5);

            double baseCharge = switch (carComingOut.getVehicleType()) {
            case 1:
                yield (numberOfMinutesSpent * 0.1);
            case 2:
                yield (numberOfMinutesSpent * 0.2);
            case 3:
                yield (numberOfMinutesSpent * 0.4);
            default:
                throw new IllegalArgumentException(
                        "No Price has been set for vehicle type: " + carComingOut.getVehicleType());
            };
            chargeForParking = baseCharge + extraCharges;
        }

        return BigDecimal.valueOf(chargeForParking).setScale(2);
    }

    private String generateBillId(ParkedCar carComingOut) {
        return carComingOut.getVehicleReg() + carComingOut.getTimeIn().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private void checkIfCarIsNotAlreadyParked(String vehicleReg) throws DuplicateCarException {
        for (int key : carPark.getCarParkSpotMappedToParkedCar().keySet()) {
            ParkedCar carParkedInCurrentSpot = carPark.getCarParkSpotMappedToParkedCar().get(key);
            if (carParkedInCurrentSpot != null && carParkedInCurrentSpot.getVehicleReg().equalsIgnoreCase(vehicleReg)) {
                throw new DuplicateCarException("A Car with this registration is already parked!");
            }
        }
    }
}
