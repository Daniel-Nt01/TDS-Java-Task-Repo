package com.dnt.tds_java_task.models;

import java.time.LocalDateTime;

public class ParkedCar {
	private String vehicleReg;
	private int vehicleType;
	private LocalDateTime timeIn;

    public ParkedCar() {
    }

	public ParkedCar(String vehicleReg, int vehicleType, LocalDateTime timeIn) {
		this.vehicleReg = vehicleReg;
		this.vehicleType = vehicleType;
		this.timeIn = timeIn;
	}

	public ParkedCar(String carRegistration, int vehicleType) {
		this.vehicleReg = carRegistration;
		this.vehicleType = vehicleType;
		this.timeIn = LocalDateTime.now();
	}

	public String getVehicleReg() {
		return vehicleReg;
	}

	public void setVehicleReg(String vehicleReg) {
		this.vehicleReg = vehicleReg;
	}

	public int getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(int vehicleType) {
		this.vehicleType = vehicleType;
	}

	public LocalDateTime getTimeIn() {
		return timeIn;
	}

	public void setTimeIn(LocalDateTime timeIn) {
		this.timeIn = timeIn;
	}

}
