package com.dnt.tds_java_task.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_DEFAULT)
public class ParkingResponse {
    private String vehicleReg;
    private int spaceNumber;
    private LocalDateTime timeIn;
    private String billId;
    private BigDecimal vehicleCharge;
    private LocalDateTime timeOut;

    public ParkingResponse() {
    }

    public ParkingResponse(String vehicleReg, int spaceNumber, LocalDateTime timeIn) {
        this.vehicleReg = vehicleReg;
        this.spaceNumber = spaceNumber;
        this.timeIn = timeIn;
    }

    public ParkingResponse(String billId, String vehicleReg, BigDecimal vehicleCharge, LocalDateTime timeIn,
            LocalDateTime timeOut) {
        this.billId = billId;
        this.vehicleReg = vehicleReg;
        this.vehicleCharge = vehicleCharge;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    }

    public String getVehicleReg() {
        return vehicleReg;
    }

    public void setVehicleReg(String vehicleReg) {
        this.vehicleReg = vehicleReg;
    }

    public int getSpaceNumber() {
        return spaceNumber;
    }

    public void setSpaceNumber(int spaceNumber) {
        this.spaceNumber = spaceNumber;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public BigDecimal getVehicleCharge() {
        return vehicleCharge;
    }

    public void setVehicleCharge(BigDecimal vehicleCharge) {
        this.vehicleCharge = vehicleCharge;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

}
