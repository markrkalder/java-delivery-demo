package com.test.delivery.exceptions;

public class UnsupportedCityException extends RuntimeException{
    /**
     * Custom exception type
     */
    public UnsupportedCityException(String message){
        super(message);
    }
}
