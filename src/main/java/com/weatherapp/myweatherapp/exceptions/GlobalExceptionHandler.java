package com.weatherapp.myweatherapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCityNotFoundException(CityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid city name: " + ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle all API-related errors.
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Map<String, Object>> handleHttpClientError(HttpClientErrorException e) {
        HttpStatusCode status = e.getStatusCode();
        Map<String, Object> response = new HashMap<>();
        if (status == HttpStatus.UNAUTHORIZED) {
            response.put("error", "Invalid API key.");
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (status == HttpStatus.TOO_MANY_REQUESTS) {
            response.put("error", "API rate limit exceeded. Try again later.");
            response.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
            return new ResponseEntity<>(response, HttpStatus.TOO_MANY_REQUESTS);
        } else {
            response.put("error", "Unexpected API error.");
            response.put("status", status.value());
            return new ResponseEntity<>(response, status);
        }
    }
}
