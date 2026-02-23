package com.dnt.tds_java_task.service;

import com.dnt.tds_java_task.customexceptions.DuplicateCarException;
import com.dnt.tds_java_task.customexceptions.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.customexceptions.VehicleNotFoundException;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;

public interface CarParkServiceInterface {
    int getNumberOfAvailableCarParkSpaces();

    void resetCarPark();

    void resetCarPark(int numberOfSpaces) throws RequiredValuesNotPassedInException;

    ParkingResponse parkNewCar(ParkedCar carToPark) throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException;

    ParkingResponse billCar(ParkedCar parkedCar) throws VehicleNotFoundException, RequiredValuesNotPassedInException;

    ParkingSpacesStatusResponse getNumberOfAvailableAndOccupiedParkingSpaces();
}
