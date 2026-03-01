package com.dnt.tds_java_task.exception;

public class VehicleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public VehicleNotFoundException() {
        super();
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }

}
