package com.test.delivery.exceptions;

public class ForbiddenVehicleTypeException extends RuntimeException{
    /**
     * Custom exception type
     */
    public ForbiddenVehicleTypeException(String message) {
        super(message);
    }
}
