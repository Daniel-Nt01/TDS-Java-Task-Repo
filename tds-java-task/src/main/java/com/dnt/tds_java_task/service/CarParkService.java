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
import com.dnt.tds_java_task.models.CarParkInterface;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;

@Component
public class CarParkService implements CarParkServiceInterface {
    private CarParkInterface carPark;

    public CarParkService() {
    }

    @Autowired
    public CarParkService(CarParkInterface carPark) {
        this.carPark = carPark;
    }

    @Override
    public int getNumberOfAvailableCarParkSpaces() {
        return (int) carPark.getCarParkSpotMappedToParkedCar().keySet().stream().filter(key -> carPark.getCarParkSpotMappedToParkedCar().get(key) == null).count();
    }

    @Override
    public void resetCarPark() {
        carPark = new CarPark();
    }

    @Override
    public void resetCarPark(int numberOfSpaces) throws RequiredValuesNotPassedInException {
        if (numberOfSpaces > 0 && numberOfSpaces < 100) {
            carPark = new CarPark(numberOfSpaces);
        }
        else {
            throw new RequiredValuesNotPassedInException("Number Of Parking Spaces Must Be Between 1 and 99 inlusive");
        }

    }

    @Override
    public ParkingResponse parkNewCar(ParkedCar carToPark)
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        if (carToPark != null && carToPark.getVehicleReg() != null && carToPark.getVehicleReg().length() >= 1
                && carToPark.getVehicleReg().length() <= 8 && (carToPark.getVehicleType() >= 1 & carToPark.getVehicleType() <= 3)) {
            
            if(!checkIfCarIsAlreadyParked(carToPark.getVehicleReg())) {
                int nextAvailableCarSpace = getNextAvailableCarSpace();
                
                ParkedCar parkedCar = carToPark.getTimeIn() != null ? new ParkedCar(carToPark.getVehicleReg(), carToPark.getVehicleType(), carToPark.getTimeIn()) 
                        : new ParkedCar(carToPark.getVehicleReg(), carToPark.getVehicleType());
                
                carPark.getCarParkSpotMappedToParkedCar().put(nextAvailableCarSpace, parkedCar);

                return new ParkingResponse(parkedCar.getVehicleReg(), nextAvailableCarSpace + 1, parkedCar.getTimeIn());
            }
            throw new DuplicateCarException("A Car with this registration is already parked!");           
        }
        else {
            throw new RequiredValuesNotPassedInException(
                    "Car Registration must be included and non null and have a length between 1 and 8 characters inclusive and Vehicle Type must also be between 1 and 3 inclusive");
        }
    }

    @Override
    public ParkingSpacesStatusResponse getNumberOfAvailableAndOccupiedParkingSpaces() {
        return new ParkingSpacesStatusResponse(getNumberOfAvailableCarParkSpaces(), getNumberOfOccupiedCarParkSpaces());
    }

    @Override
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
        return carPark.getCarParkSpotMappedToParkedCar().keySet().stream().filter(key -> carPark.getCarParkSpotMappedToParkedCar().get(key) == null).findFirst()
                .orElseThrow(() -> new NoAvailableCarSpaceException("No Available Car Spaces"));
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

    private BigDecimal calculateAccumulatedChargeForParking(ParkedCar carComingOut, LocalDateTime timeOut) {
        double chargeForParking = 0.00;
        long numberOfMinutesSpent = ChronoUnit.MINUTES.between(carComingOut.getTimeIn(), timeOut);
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
        
        
        return new BigDecimal(String.valueOf(chargeForParking)).setScale(2);
    }

    private String generateBillId(ParkedCar carComingOut) {
        return carComingOut.getVehicleReg() + carComingOut.getTimeIn().toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    private boolean checkIfCarIsAlreadyParked(String vehicleReg) throws DuplicateCarException {
        return carPark.getCarParkSpotMappedToParkedCar().keySet().stream()
                .anyMatch(key -> carPark.getCarParkSpotMappedToParkedCar().get(key) != null && carPark.getCarParkSpotMappedToParkedCar().get(key).getVehicleReg().equalsIgnoreCase(vehicleReg));
    }
}
