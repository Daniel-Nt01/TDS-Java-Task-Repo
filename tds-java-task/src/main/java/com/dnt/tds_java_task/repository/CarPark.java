package com.dnt.tds_java_task.repository;

import java.util.Map;

import com.dnt.tds_java_task.dto.request.ParkedCarRequest;

public interface CarPark {

    Map<Integer, ParkedCarRequest> getCarParkSpotAndParkedCarMap();
    
    int getNumberOfAvailableCarParkSpaces();
    
    int getTotalNumberOfCarParkSpaces();
    
    int getNextAvailableCarSpace();
    
    ParkedCarRequest addCarToCarPark(int carParkSpot, ParkedCarRequest carToPark);
    
    ParkedCarRequest removeCarFromCarPark(int carParkSpot);
    
    int getCarParkSpotOfParkedCar(String carRegistration);

}
