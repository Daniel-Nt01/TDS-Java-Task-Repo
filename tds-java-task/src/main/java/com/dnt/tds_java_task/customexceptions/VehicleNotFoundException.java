package com.dnt.tds_java_task.customexceptions;

@SuppressWarnings("serial")
public class VehicleNotFoundException extends Exception {

    public VehicleNotFoundException() {
        super();
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }

}
