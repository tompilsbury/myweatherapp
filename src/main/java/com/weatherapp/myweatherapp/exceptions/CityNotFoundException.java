package com.weatherapp.myweatherapp.exceptions;

public class CityNotFoundException extends RuntimeException {
    public CityNotFoundException(String city) {
        super(city);
    }
}
