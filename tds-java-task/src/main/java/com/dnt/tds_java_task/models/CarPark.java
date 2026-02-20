package com.dnt.tds_java_task.models;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CarPark {
    private Map<Integer, ParkedCar> carParkSpotMappedToParkedCar;

    public CarPark() {
        carParkSpotMappedToParkedCar = new HashMap<Integer, ParkedCar>(30);
        createNumberOfEmptyCarParksSpacesByPassedNumberOfSpacesRequestedToHold(30);
    }

    public CarPark(int numberOfParkingSpaces) {
        carParkSpotMappedToParkedCar = new HashMap<Integer, ParkedCar>(numberOfParkingSpaces);
        createNumberOfEmptyCarParksSpacesByPassedNumberOfSpacesRequestedToHold(numberOfParkingSpaces);
    }

    public Map<Integer, ParkedCar> getCarParkSpotMappedToParkedCar() {
        return carParkSpotMappedToParkedCar;
    }

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
