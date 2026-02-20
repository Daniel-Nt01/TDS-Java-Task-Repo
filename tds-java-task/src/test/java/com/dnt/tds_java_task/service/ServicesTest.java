package com.dnt.tds_java_task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.models.CarPark;
import com.dnt.tds_java_task.models.ParkedCar;

public class ServicesTest {

    @Test
    void carParkServiceShouldNotBeNull_WhenUsingDefaultConstructor() {
        CarParkService carParkService = new CarParkService();
        assertNotNull(carParkService);
    }

    @Test
    void carParkServiceShouldNotBeNull_WhenUsingConstructorWithPassedInValue() {
        CarParkService carParkService = new CarParkService(new CarPark());
        assertNotNull(carParkService);
    }

    @Test
    void carParkServiceShouldHave30AvailableSpaces_AfterCallingTheResetCarParkMethod() {
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarPark();
        assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 30);
    }

    @Test
    void carParkServiceShouldHaveANumberOfAvailableSpaceEqualingThePassedInNumberOfSpaces_AfterCallingTheResetCarParkMethodPassingInNumberOfSpaces()
            throws RequiredValuesNotPassedInException {
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarPark(80);
        assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 80);
    }

    @Test
    void throwNoAvailableCarSpaceExceptionWhenAttemptingToParkWithACarWithNoReg()
            throws RequiredValuesNotPassedInException {
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarPark(1);
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        ParkedCar parkedCar2 = new ParkedCar();
        parkedCar2.setVehicleReg("XXXXX2");
        parkedCar.setVehicleType(1);

        try {
            carParkService.parkNewCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof RequiredValuesNotPassedInException);
        }
    }
}
