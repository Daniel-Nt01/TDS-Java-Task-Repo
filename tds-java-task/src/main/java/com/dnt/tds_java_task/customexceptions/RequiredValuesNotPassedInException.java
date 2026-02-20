package com.dnt.tds_java_task.customexceptions;

@SuppressWarnings("serial")
public class RequiredValuesNotPassedInException extends Exception {

    public RequiredValuesNotPassedInException() {
        super();
    }

    public RequiredValuesNotPassedInException(String message) {
        super(message);
    }

}
