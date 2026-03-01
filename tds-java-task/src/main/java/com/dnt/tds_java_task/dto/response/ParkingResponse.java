package com.dnt.tds_java_task.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public record ParkingResponse(String vehicleReg, int spaceNumber, LocalDateTime timeIn, String billId, LocalDateTime timeOut, BigDecimal vehicleCharge) {
    public ParkingResponse(String vehicleReg, int spaceNumber, LocalDateTime timeIn) {
        this(vehicleReg, spaceNumber, timeIn, null, null, null);
    }
    
    public ParkingResponse(String billId, String vehicleReg, BigDecimal vehicleCharge, LocalDateTime timeIn, LocalDateTime timeOut) {
        this(vehicleReg, 0, timeIn, billId, timeOut, vehicleCharge);
    }

}
