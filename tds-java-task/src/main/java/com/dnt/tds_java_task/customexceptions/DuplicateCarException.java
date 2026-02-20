package com.dnt.tds_java_task.customexceptions;

@SuppressWarnings("serial")
public class DuplicateCarException extends Exception {

    public DuplicateCarException() {
        super();
    }

    public DuplicateCarException(String message) {
        super(message);
    }

}
