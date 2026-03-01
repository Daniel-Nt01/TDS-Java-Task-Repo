package com.dnt.tds_java_task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.exception.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.repository.CarParkRepo;

public class ServicesTest {

    @Test
    void carParkServiceShouldNotBeNull_WhenUsingDefaultConstructor() {
        CarParkService carParkService = new CarParkService();
        assertNotNull(carParkService);
    }

    @Test
    void carParkServiceShouldNotBeNull_WhenUsingConstructorWithPassedInValue() {
        CarParkService carParkService = new CarParkService(new CarParkRepo());
        assertNotNull(carParkService);
    }

    @Test
    void carParkServiceShouldHave30AvailableSpaces_AfterCallingTheResetCarParkMethod() {
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarPark();
        assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 30);
    }

    @Test
    void carParkServiceShouldHaveANumberOfAvailableSpaceEqualingThePassedInNumberOfSpaces_AfterCallingTheResetCarParkMethodPassingInNumberOfSpaces(){
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarParkWithNumberOfSpaces(80);
        assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 80);
    }

    @Test
    void throwNoAvailableCarSpaceExceptionWhenAttemptingToParkWhenAlreadyFull(){
        CarParkService carParkService = new CarParkService();
        carParkService.resetCarParkWithNumberOfSpaces(1);
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, null);

        ParkedCarRequest parkedCar2 = new ParkedCarRequest("XXXXXX2", 1, null);

        try {
            carParkService.parkNewCar(parkedCar);
            assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 0);
            carParkService.parkNewCar(parkedCar2);
        }
        catch (Exception e) {
            assertTrue(e instanceof NoAvailableCarSpaceException);
        }
    }
}
