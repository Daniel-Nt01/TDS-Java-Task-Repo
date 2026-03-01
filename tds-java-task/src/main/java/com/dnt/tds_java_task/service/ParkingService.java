package com.dnt.tds_java_task.service;

import com.dnt.tds_java_task.dto.request.CarToRemoveRequest;
import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.dto.response.ParkingResponse;
import com.dnt.tds_java_task.dto.response.ParkingSpacesStatusResponse;
import com.dnt.tds_java_task.exception.DuplicateCarException;
import com.dnt.tds_java_task.exception.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.exception.VehicleNotFoundException;

public interface ParkingService {
    int getNumberOfAvailableCarParkSpaces();

    void resetCarPark();

    void resetCarParkWithNumberOfSpaces(int numberOfSpaces);

    ParkingResponse parkNewCar(ParkedCarRequest carToPark) throws NoAvailableCarSpaceException, DuplicateCarException;

    ParkingResponse billCar(CarToRemoveRequest carToRemove) throws VehicleNotFoundException;

    ParkingSpacesStatusResponse getNumberOfAvailableAndOccupiedParkingSpaces();
}
