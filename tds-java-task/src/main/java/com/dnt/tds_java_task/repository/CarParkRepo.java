package com.dnt.tds_java_task.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.exception.NoAvailableCarSpaceException;

@Repository
public class CarParkRepo implements CarPark {
    private Map<Integer, ParkedCarRequest> carParkSpotAndParkedCarMap;

    public CarParkRepo() {
        carParkSpotAndParkedCarMap = new HashMap<Integer, ParkedCarRequest>(30);
        instantiateEmptyCarParkWithNumberOfSpaces(30);
    }

    public CarParkRepo(int numberOfParkingSpaces) {
        carParkSpotAndParkedCarMap = new HashMap<Integer, ParkedCarRequest>(numberOfParkingSpaces);
        instantiateEmptyCarParkWithNumberOfSpaces(numberOfParkingSpaces);
    }

    @Override
    public Map<Integer, ParkedCarRequest> getCarParkSpotAndParkedCarMap() {
        return carParkSpotAndParkedCarMap;
    }
    
    @Override
    public int getNumberOfAvailableCarParkSpaces() {
        return (int) carParkSpotAndParkedCarMap.keySet().stream().filter(key -> carParkSpotAndParkedCarMap.get(key) == null).count();
    }
    
    @Override
    public int getTotalNumberOfCarParkSpaces() {
        return carParkSpotAndParkedCarMap.size();
    }
    
    @Override
    public int getNextAvailableCarSpace() {
        return carParkSpotAndParkedCarMap.keySet().stream().filter(key -> carParkSpotAndParkedCarMap.get(key) == null)
                .findFirst()
                .orElseThrow(() -> new NoAvailableCarSpaceException("No Available Car Spaces"));
    }
    
    @Override
    public ParkedCarRequest addCarToCarPark(int carParkSpot, ParkedCarRequest carToPark) {
        return carParkSpotAndParkedCarMap.put(carParkSpot, carToPark);
    }
    
    @Override
    public ParkedCarRequest removeCarFromCarPark(int carParkSpot) {
        return carParkSpotAndParkedCarMap.put(carParkSpot, null);
    }
    
    @Override
    public int getCarParkSpotOfParkedCar(String carRegistration) {
        return carParkSpotAndParkedCarMap.keySet().stream()
                .filter(key -> carParkSpotAndParkedCarMap.get(key) != null && carParkSpotAndParkedCarMap.get(key).vehicleReg().equals(carRegistration))
                .findFirst()
                .orElse(-1);
    }

    private void instantiateEmptyCarParkWithNumberOfSpaces(int numberOfSpaces) {
        if (numberOfSpaces < 100) {
            for (int index = 0; index < numberOfSpaces; index++) {
                carParkSpotAndParkedCarMap.put(index, null);
            }
        }
    }
}
