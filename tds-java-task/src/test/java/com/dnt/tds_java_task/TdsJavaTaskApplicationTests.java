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

import com.dnt.tds_java_task.customexceptions.DuplicateCarException;
import com.dnt.tds_java_task.customexceptions.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.customexceptions.VehicleNotFoundException;
import com.dnt.tds_java_task.models.ParkedCar;
import com.dnt.tds_java_task.models.ParkingResponse;
import com.dnt.tds_java_task.models.ParkingSpacesStatusResponse;
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
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        ParkedCar parkedCar2 = new ParkedCar();
        parkedCar2.setVehicleReg("XXXXX2");
        parkedCar2.setVehicleType(1);

        int numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 30);
        carParkService.parkNewCar(parkedCar);
        carParkService.parkNewCar(parkedCar2);

        numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 28);
    }

    @Test
    void throwRequiredValuesNotPassedInExceptionWhenAttemptingToParkWithACarWithNoReg() {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleType(1);

        try {
            carParkService.parkNewCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof RequiredValuesNotPassedInException);
        }
    }

    @Test
    void throwDuplicateCarExceptionWhenAttemptingToParkWithACarRegistrationThatIsAlreadyParked() {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        ParkedCar parkedCar2 = parkedCar;
        parkedCar2.setVehicleType(3);

        try {
            carParkService.parkNewCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof DuplicateCarException);
        }
    }

    @Test
    void parkedResponseShouldBeReturnedWithATimeInDate_VehicleReg_SpaceNumber_WhenACarIsParked()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        Object object = carParkService.parkNewCar(parkedCar);

        assertTrue(object instanceof ParkingResponse);

        ParkingResponse parkingResponse = (ParkingResponse) object;

        assertNotNull(parkingResponse.getVehicleReg());
        assertNotNull(parkingResponse.getTimeIn());
        assertEquals(parkingResponse.getSpaceNumber(), 1);
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

        assertEquals(parkingSpacesStatusResponse.getAvailableSpaces(), 30);
        assertEquals(parkingSpacesStatusResponse.getOccupiedSpaces(), 0);

    }

    @Test
    void throwVehicleNotFoundExceptionWhenAttemptingToBillACarThatIsNotParked() {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");

        try {
            carParkService.billCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof VehicleNotFoundException);
        }
    }

    @Test
    void throwRequiredValuesNotPassedInExceptionWhenAttemptingToBillACarWithoutPassingInARegistration() {
        ParkedCar parkedCar = new ParkedCar();

        try {
            carParkService.billCar(parkedCar);
        }
        catch (Exception e) {
            assertTrue(e instanceof RequiredValuesNotPassedInException);
        }
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAParkedCar_ShouldNotBeNull() throws NoAvailableCarSpaceException,
            RequiredValuesNotPassedInException, VehicleNotFoundException, DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        carParkService.parkNewCar(parkedCar);
        ParkingResponse parkingResponse = carParkService.billCar(parkedCar);

        assertNotNull(parkingResponse);
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAParkedCar_ShouldHaveNonNullValuesForExpectedAttributesWhenParkedForAMinute()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar("XXXXXX", 1, LocalDateTime.now().minusMinutes(1));

        carParkService.parkNewCar(parkedCar);
        ParkingResponse parkingResponse = carParkService.billCar(parkedCar);

        assertNotNull(parkingResponse.getBillId());
        assertNotNull(parkingResponse.getVehicleReg());
        assertNotNull(parkingResponse.getVehicleCharge());
        assertNotNull(parkingResponse.getTimeIn());
        assertNotNull(parkingResponse.getTimeOut());
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingASmallParkedCar_ShouldHaveAChargeOfThreeWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar("XXXXXX", 1, LocalDateTime.now().minusMinutes(10));

        carParkService.parkNewCar(parkedCar);
        ParkingResponse parkingResponse = carParkService.billCar(parkedCar);

        assertEquals(parkingResponse.getVehicleCharge(), new BigDecimal("3.00"));
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingAMediumParkedCar_ShouldHaveAChargeOfFourWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar("XXXXXX", 2, LocalDateTime.now().minusMinutes(10));

        carParkService.parkNewCar(parkedCar);
        ParkingResponse parkingResponse = carParkService.billCar(parkedCar);

        assertEquals(parkingResponse.getVehicleCharge(), new BigDecimal("4.00"));
    }

    @Test
    void parkedResponseReturnedFromBillCarWhenUsingALargeParkedCar_ShouldHaveAChargeOfSixWhenParkedForTenMinutes()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar("XXXXXX", 3, LocalDateTime.now().minusMinutes(10));

        carParkService.parkNewCar(parkedCar);
        ParkingResponse parkingResponse = carParkService.billCar(parkedCar);

        assertEquals(parkingResponse.getVehicleCharge(), new BigDecimal("6.00"));
    }

    @Test
    void numberOfAvailableCarParkSpacesFoundViaCarParkServiceShouldBeOneLessFromTheOriginal_AfterParkingTwoCarsAndHavingOneOfThemBilled()
            throws NoAvailableCarSpaceException, RequiredValuesNotPassedInException, VehicleNotFoundException,
            DuplicateCarException {
        ParkedCar parkedCar = new ParkedCar();
        parkedCar.setVehicleReg("XXXXXX");
        parkedCar.setVehicleType(1);

        ParkedCar parkedCar2 = new ParkedCar();
        parkedCar2.setVehicleReg("XXXXX2");
        parkedCar2.setVehicleType(2);

        int numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 30);
        carParkService.parkNewCar(parkedCar);
        carParkService.parkNewCar(parkedCar2);

        carParkService.billCar(parkedCar);
        numberOfAvailableSpaces = carParkService.getNumberOfAvailableCarParkSpaces();
        assertEquals(numberOfAvailableSpaces, 29);
    }

}
