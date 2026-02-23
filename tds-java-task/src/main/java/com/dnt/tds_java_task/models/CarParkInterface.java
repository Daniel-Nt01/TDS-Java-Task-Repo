package com.dnt.tds_java_task.models;

import java.util.Map;

public interface CarParkInterface {

    Map<Integer, ParkedCar> getCarParkSpotMappedToParkedCar();

    void setCarParkSpotMappedToParkedCar(Map<Integer, ParkedCar> carParkSpotMappedToParkedCar);
}
