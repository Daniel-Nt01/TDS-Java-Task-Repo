package com.dnt.tds_java_task.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.dnt.tds_java_task.models.CarPark;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;

public class ModelsTest {

    @Test
    void parkedCarShouldBeCreated_WhenUsingDefaultConstructor() {
        ParkedCar parkedCar = new ParkedCar();
        assertNotNull(parkedCar);
    }

    @Test
    void parkedCarShouldBeCreated_WhenUsingConstructorWithPassedInValues() {
        ParkedCar parkedCar = new ParkedCar("Default License", 1, LocalDateTime.now());
        assertNotNull(parkedCar);
    }

    @Test
    void parkedCarAttributesShouldBeNullOrDefaultValue_WhenUsingDefaultConstructor() {
        ParkedCar parkedCar = new ParkedCar();
        assertNull(parkedCar.getVehicleReg());
        assertEquals(0, parkedCar.getVehicleType());
        assertNull(parkedCar.getTimeIn());
    }

    @Test
    void parkedCarAttributesShouldNotBeNullOrDefaultValue_WhenUsingConstructorWithPassedInValuesThatAreNotNullOrTheDefaultValue() {
        ParkedCar parkedCar = new ParkedCar("Default License", 1, LocalDateTime.now());
        assertNotNull(parkedCar.getVehicleReg());
        assertNotEquals(0, parkedCar.getVehicleType());
        assertNotNull(parkedCar.getTimeIn());
    }

    @Test
    void carParkShouldBeCreated_WhenUsingDefaultConstructor() {
        CarPark carPark = new CarPark();
        assertNotNull(carPark);
    }

    @Test
    void carParkShouldBeCreated_WhenUsingConstructorWithPassedInValue() {
        CarPark carPark = new CarPark(30);
        assertNotNull(carPark);
    }

    @Test
    void carParkAttributesShouldNotBeNull_WhenUsingDefaultConstructor() {
        CarPark carPark = new CarPark();
        assertNotNull(carPark.getCarParkSpotMappedToParkedCar());
    }

    @Test
    void carParkAttributesShouldNotBeNull_WhenUsingConstructorWithPassedInValue() {
        CarPark carPark = new CarPark(40);
        assertNotNull(carPark.getCarParkSpotMappedToParkedCar());
    }

    @Test
    void numberOfCarParkSpotsShouldEqualThirty_WhenUsingDefaultConstructor() {
        CarPark carPark = new CarPark();
        assertEquals(carPark.getCarParkSpotMappedToParkedCar().keySet().size(), 30);
    }

    @Test
    void numberOfCarParkSpotsShouldEqualPassedInValue_WhenUsingConstructorWithPassedInValue() {
        CarPark carPark = new CarPark(60);
        assertEquals(carPark.getCarParkSpotMappedToParkedCar().keySet().size(), 60);
    }

    @Test
    void numberOfCarParkSpotsShouldEqualZero_WhenUsingConstructorWithPassedInValueOverNinetyNine() {
        CarPark carPark = new CarPark(100);
        assertEquals(carPark.getCarParkSpotMappedToParkedCar().keySet().size(), 0);
    }

    @Test
    void numberOfCarParkSpotsShouldThrowAnIllegalArguementException_WhenUsingConstructorWithPassedInValueLessThan0() {
        try {
            new CarPark(-1);
        }
        catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }

    }

    @Test
    void parkingResponseShouldBeCreated_WhenUsingDefaultConstructor() {
        ParkingResponse parkingResponse = new ParkingResponse();
        assertNotNull(parkingResponse);
    }

    @Test
    void parkingResponseShouldBeCreated_WhenUsingConstructorWithPassedInValues() {
        ParkingResponse parkingResponse = new ParkingResponse("RandomVehicleReg", 12, LocalDateTime.now());
        assertNotNull(parkingResponse);
    }

    @Test
    void parkingResponseAttributesUsingTheVehicleReg_SpaceNumber_TimeIn_ShouldNotBeNullOrDefaultValue_WhenUsingConstructorWithThesePassedInValues() {
        ParkingResponse parkingResponse = new ParkingResponse("RandomVehicleReg", 12, LocalDateTime.now());
        assertNotNull(parkingResponse.getVehicleReg());
        assertEquals(parkingResponse.getSpaceNumber(), 12);
        assertNotNull(parkingResponse.getTimeIn());
    }

    @Test
    void parkedCarAttributesShouldNotBeNullOrDefaultValue_WhenUsingConstructorWithPassedInValuesThatAreNotNullOrTheDefaultValueAndNotPassingInTimeInDate() {
        ParkedCar parkedCar = new ParkedCar("Default License", 1);
        assertNotNull(parkedCar.getVehicleReg());
        assertNotEquals(parkedCar.getVehicleType(), 0);
        assertNotNull(parkedCar.getTimeIn());
    }

    @Test
    void parkingSpacesStatusResponseShouldBeCreated_WhenUsingConstructorWithPassedInValuesforAvailableAndOccupiedSpaces() {
        ParkingSpacesStatusResponse parkingResponse = new ParkingSpacesStatusResponse(20, 0);
        assertNotNull(parkingResponse);
    }

    @Test
    void parkingSpacesStatusResponseAttributesUsingAvailableSpaces_OccupiedSpacesShouldntUseDefaultValue_WhenUsingConstructorWithThesePassedInValuesOver0() {
        ParkingSpacesStatusResponse parkingSpacesStatusResponse = new ParkingSpacesStatusResponse(20, 10);
        assertNotEquals(parkingSpacesStatusResponse.getAvailableSpaces(), 0);
        assertNotEquals(parkingSpacesStatusResponse.getOccupiedSpaces(), 0);
    }

    @Test
    void parkingSpacesStatusResponseAttributesUsingAvailableSpaces_OccupiedSpacesShouldEqualPassedInValues_WhenUsingConstructorWithThesePassedInValues() {
        ParkingSpacesStatusResponse parkingSpacesStatusResponse = new ParkingSpacesStatusResponse(20, 10);
        assertEquals(parkingSpacesStatusResponse.getAvailableSpaces(), 20);
        assertEquals(parkingSpacesStatusResponse.getOccupiedSpaces(), 10);
    }

    @Test
    void parkingResponseShouldBeCreated_WhenUsingConstructorWithPassedInValuesForBillID_VehicleReg_VehicleCharge_TimeIn_TimeOut() {
        ParkingResponse parkingResponse = new ParkingResponse("ID1", "RandomVehicleReg", new BigDecimal("89.0"),
                LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(parkingResponse);
    }

    @Test
    void parkingResponseAttributesBeingUpdatedShouldNotUseNull_WhenUsingConstructorWithPassedInValuesForBillID_VehicleReg_VehicleCharge_TimeIn_TimeOut() {
        ParkingResponse parkingResponse = new ParkingResponse("ID1", "RandomVehicleReg", new BigDecimal("89.0"),
                LocalDateTime.now(), LocalDateTime.now());
        assertNotNull(parkingResponse.getBillId());
        assertNotNull(parkingResponse.getVehicleReg());
        assertNotNull(parkingResponse.getVehicleCharge());
        assertNotNull(parkingResponse.getTimeIn());
        assertNotNull(parkingResponse.getTimeOut());
    }
}
