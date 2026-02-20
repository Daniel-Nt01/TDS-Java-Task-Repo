package com.dnt.tds_java_task.models;

public class ParkingSpacesStatusResponse {

    private int availableSpaces;
    private int occupiedSpaces;

    public ParkingSpacesStatusResponse() {
    }

    public ParkingSpacesStatusResponse(int availableSpaces, int occupiedSpaces) {
        this.availableSpaces = availableSpaces;
        this.occupiedSpaces = occupiedSpaces;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public void setAvailableSpaces(int availableSpaces) {
        this.availableSpaces = availableSpaces;
    }

    public int getOccupiedSpaces() {
        return occupiedSpaces;
    }

    public void setOccupiedSpaces(int occupiedSpaces) {
        this.occupiedSpaces = occupiedSpaces;
    }
}
