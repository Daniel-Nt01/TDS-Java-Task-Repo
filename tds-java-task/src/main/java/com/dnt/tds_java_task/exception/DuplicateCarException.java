package com.dnt.tds_java_task.exception;

public class DuplicateCarException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    public DuplicateCarException() {
        super();
    }

    public DuplicateCarException(String message) {
        super(message);
    }

}
