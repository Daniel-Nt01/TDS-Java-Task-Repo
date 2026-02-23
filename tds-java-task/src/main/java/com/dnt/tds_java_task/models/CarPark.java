package com.dnt.tds_java_task.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CarPark implements CarParkInterface {
    private Map<Integer, ParkedCar> carParkSpotMappedToParkedCar;

    public CarPark() {
        carParkSpotMappedToParkedCar = new HashMap<Integer, ParkedCar>(30);
        createNumberOfEmptyCarParksSpacesByPassedNumberOfSpacesRequestedToHold(30);
    }

    public CarPark(int numberOfParkingSpaces) {
        carParkSpotMappedToParkedCar = new HashMap<Integer, ParkedCar>(numberOfParkingSpaces);
        createNumberOfEmptyCarParksSpacesByPassedNumberOfSpacesRequestedToHold(numberOfParkingSpaces);
    }

    @Override
    public Map<Integer, ParkedCar> getCarParkSpotMappedToParkedCar() {
        return carParkSpotMappedToParkedCar;
    }

    @Override
    public void setCarParkSpotMappedToParkedCar(Map<Integer, ParkedCar> carParkSpotMappedToParkedCar) {
        this.carParkSpotMappedToParkedCar = carParkSpotMappedToParkedCar;
    }

    private void createNumberOfEmptyCarParksSpacesByPassedNumberOfSpacesRequestedToHold(int numberOfSpaces) {
        if (numberOfSpaces < 100) {
            for (int index = 0; index < numberOfSpaces; index++) {
                carParkSpotMappedToParkedCar.put(index, null);
            }
        }
    }
}
