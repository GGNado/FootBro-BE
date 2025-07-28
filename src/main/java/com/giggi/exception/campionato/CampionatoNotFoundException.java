package com.giggi.exception.campionato;

public class CampionatoNotFoundException extends RuntimeException {
    public CampionatoNotFoundException(String message) {
        super(message);
    }
}