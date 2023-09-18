package com.test.delivery.exceptions;

public class UnsupportedVehicleTypeException extends RuntimeException{
    /**
     * Custom exception type
     */
    public UnsupportedVehicleTypeException(String message){
        super(message);
    }
}
