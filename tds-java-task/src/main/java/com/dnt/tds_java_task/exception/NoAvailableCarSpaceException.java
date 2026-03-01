package com.dnt.tds_java_task.exception;

public class NoAvailableCarSpaceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public NoAvailableCarSpaceException() {
        super();
    }

    public NoAvailableCarSpaceException(String message) {
        super(message);
    }

}
