package com.dnt.tds_java_task.customexceptions;

@SuppressWarnings("serial")
public class NoAvailableCarSpaceException extends Exception {

    public NoAvailableCarSpaceException() {
        super();
    }

    public NoAvailableCarSpaceException(String message) {
        super(message);
    }

}
