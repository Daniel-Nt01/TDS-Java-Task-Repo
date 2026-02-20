package com.dnt.tds_java_task.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dnt.tds_java_task.customexceptions.DuplicateCarException;
import com.dnt.tds_java_task.customexceptions.NoAvailableCarSpaceException;
import com.dnt.tds_java_task.customexceptions.RequiredValuesNotPassedInException;
import com.dnt.tds_java_task.customexceptions.VehicleNotFoundException;

@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(NoAvailableCarSpaceException.class)
    public ResponseEntity<String> handleNoAvailableCarSpaceExceptions(NoAvailableCarSpaceException ex) {
        return ResponseEntity.status(412).body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequiredValuesNotPassedInException.class)
    public ResponseEntity<String> handleRequiredValuesNotPassedInExceptions(RequiredValuesNotPassedInException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<String> handleVehicleNotFoundExceptions(VehicleNotFoundException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateCarException.class)
    public ResponseEntity<String> handleDuplicateCarExceptions(DuplicateCarException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableExceptions(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Request Body Is Missing, This Is Required For This Request!");
    }
}
