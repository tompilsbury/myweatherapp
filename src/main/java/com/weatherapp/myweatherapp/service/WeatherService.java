package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.exceptions.CityNotFoundException;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  public CityInfo forecastByCity(String city) {
    try {
      return weatherRepo.getByCity(city);
    } catch (HttpClientErrorException e) {
      // API returns a BAD_REQUEST error from invalid city name.
      if (e.getStatusCode() == HttpStatus.BAD_REQUEST){
        throw new CityNotFoundException(city);
      }

      // For any other API error, rethrow it and GlobalExceptionHandler will handle it.
      else {
        throw e;
      }
    }
  }

  public CityInfo compareDaylight(String city1, String city2) {
    CityInfo ci1 = forecastByCity(city1);
    CityInfo ci2 = forecastByCity(city2);

    return (ci1.getDaylightMinutes() >= ci2.getDaylightMinutes()) ? ci1 : ci2;
  }

}
