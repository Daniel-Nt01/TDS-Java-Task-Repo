package com.dnt.tds_java_task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.dnt.tds_java_task.dto.request.CarToRemoveRequest;
import com.dnt.tds_java_task.dto.request.ParkedCarRequest;
import com.dnt.tds_java_task.dto.response.ParkingResponse;
import com.dnt.tds_java_task.dto.response.ParkingSpacesStatusResponse;
import com.dnt.tds_java_task.exception.DuplicateCarException;
import com.dnt.tds_java_task.exception.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.exception.VehicleNotFoundException;
import com.dnt.tds_java_task.service.CarParkService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TdsJavaTaskApplicationTests {

    @Autowired
    private CarParkService carParkService;

    @Test
    void numberOfAvailableCarParkSpacesFoundViaCarParkServiceShouldBeThirty_WhenHavingNotUpdatedTheCarParkSizeAndNoCarsHaveBeenParked() {
        assertEquals(carParkService.getNumberOfAvailableCarParkSpaces(), 30);
    }

    @Test
    void numberOfAvailableCarParkSpacesFoundViaCarParkServiceShouldBeTwoLess_AfterParkingTwoCars()
            throws NoAvailableCarSpaceException, DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, null);

        ParkedCarRequest parkedCar2 = new ParkedCarRequest("XXXXXX2", 1, null);

        int numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 30);
        carParkService.parkNewCar(parkedCar);
        carParkService.parkNewCar(parkedCar2);

        numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 28);
    }

    @Test
    void throwRequiredValuesNotPassedInExceptionWhenAttemptingToParkWithACarWithNoReg() {
        ParkedCarRequest parkedCar = new ParkedCarRequest(null, 1, null);

        try {
            carParkService.parkNewCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    void throwDuplicateCarExceptionWhenAttemptingToParkWithACarRegistrationThatIsAlreadyParked() {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, null);

        ParkedCarRequest parkedCar2 = new ParkedCarRequest(parkedCar.vehicleReg(), 3, null);

        try {
            carParkService.parkNewCar(parkedCar);
            carParkService.parkNewCar(parkedCar2);
        }
        catch (Exception e) {
            assertTrue(e instanceof DuplicateCarException);
        }
    }

    @Test
    void parkedResponseShouldBeReturnedWithATimeInDate_VehicleReg_SpaceNumber_WhenACarIsParked()
            throws NoAvailableCarSpaceException, DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, null);

        Object object = carParkService.parkNewCar(parkedCar);

        assertTrue(object instanceof ParkingResponse);

        ParkingResponse parkingResponse = (ParkingResponse) object;

        assertNotNull(parkingResponse.vehicleReg());
        assertNotNull(parkingResponse.timeIn());
        assertEquals(parkingResponse.spaceNumber(), 1);
    }

    @Test
    void parkingSpacesStatusResponseReturnedFrom_GetNumberOfAvailableAndOccupiedParkingSpaces_ShouldNotBeNull() {
        Object object = carParkService.getNumberOfAvailableAndOccupiedParkingSpaces();

        assertTrue(object instanceof ParkingSpacesStatusResponse);

        ParkingSpacesStatusResponse parkingResponse = (ParkingSpacesStatusResponse) object;

        assertNotNull(parkingResponse);
    }

    @Test
    void parkingSpacesStatusResponseReturnedFrom_GetNumberOfAvailableAndOccupiedParkingSpaces_ShouldHaveAnAvailableNumberOfThirtyAndOccupiedNumberOf0WhenNoCarsHaveBeenParkedAndUsingDefaultCarPark() {
        ParkingSpacesStatusResponse parkingSpacesStatusResponse = carParkService
                .getNumberOfAvailableAndOccupiedParkingSpaces();

        assertEquals(parkingSpacesStatusResponse.availableSpaces(), 30);
        assertEquals(parkingSpacesStatusResponse.occupiedSpaces(), 0);

    }

    @Test
    void throwVehicleNotFoundExceptionWhenAttemptingToBillACarThatIsNotParked() {
        CarToRemoveRequest parkedCar = new CarToRemoveRequest("XXXXXX");

        try {
            carParkService.billCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof VehicleNotFoundException);
        }
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAParkedCar_ShouldNotBeNull() throws NoAvailableCarSpaceException, VehicleNotFoundException, DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, null);

        carParkService.parkNewCar(parkedCar);
        CarToRemoveRequest carToRemove = new CarToRemoveRequest("XXXXXX");
        ParkingResponse parkingResponse = carParkService.billCar(carToRemove);

        assertNotNull(parkingResponse);
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAParkedCar_ShouldHaveNonNullValuesForExpectedAttributesWhenParkedForAMinute()
            throws NoAvailableCarSpaceException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, LocalDateTime.now().minusMinutes(1));

        carParkService.parkNewCar(parkedCar);
        
        CarToRemoveRequest carToRemove = new CarToRemoveRequest(parkedCar.vehicleReg());
        ParkingResponse parkingResponse = carParkService.billCar(carToRemove);

        assertNotNull(parkingResponse.billId());
        assertNotNull(parkingResponse.vehicleReg());
        assertNotNull(parkingResponse.vehicleCharge());
        assertNotNull(parkingResponse.timeIn());
        assertNotNull(parkingResponse.timeOut());
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingASmallParkedCar_ShouldHaveAChargeOfThreeWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, LocalDateTime.now().minusMinutes(10));

        carParkService.parkNewCar(parkedCar);
        
        CarToRemoveRequest carToRemove = new CarToRemoveRequest(parkedCar.vehicleReg());
        ParkingResponse parkingResponse = carParkService.billCar(carToRemove);

        assertEquals(parkingResponse.vehicleCharge(), new BigDecimal("3.00"));
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAMediumParkedCar_ShouldHaveAChargeOfFourWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 2, LocalDateTime.now().minusMinutes(10));

        carParkService.parkNewCar(parkedCar);
        CarToRemoveRequest carToRemove = new CarToRemoveRequest(parkedCar.vehicleReg());
        ParkingResponse parkingResponse = carParkService.billCar(carToRemove);

        assertEquals(parkingResponse.vehicleCharge(), new BigDecimal("4.00"));
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingALargeParkedCar_ShouldHaveAChargeOfSixWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 3, LocalDateTime.now().minusMinutes(10));
        carParkService.parkNewCar(parkedCar);
        
        CarToRemoveRequest carToRemove = new CarToRemoveRequest(parkedCar.vehicleReg());
        ParkingResponse parkingResponse = carParkService.billCar(carToRemove);

        assertEquals(parkingResponse.vehicleCharge(), new BigDecimal("6.00"));
    }

    @Test
    void numberOfAvailableCarParkSpacesFoundViaCarParkServiceShouldBeOneLessFromTheOriginal_AfterParkingTwoCarsAndHavingOneOfThemBilled()
            throws NoAvailableCarSpaceException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCarRequest parkedCar = new ParkedCarRequest("XXXXXX", 1, LocalDateTime.now());

        ParkedCarRequest parkedCar2 = new ParkedCarRequest("XXXXXX2", 2, LocalDateTime.now());

        int numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 30);
        carParkService.parkNewCar(parkedCar);
        carParkService.parkNewCar(parkedCar2);

        CarToRemoveRequest carToRemove = new CarToRemoveRequest(parkedCar.vehicleReg());
        carParkService.billCar(carToRemove);
        numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 29);
    }

}
