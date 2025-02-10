package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  public CityInfo forecastByCity(String city) {

    return weatherRepo.getByCity(city);
  }

  public CityInfo compareDaylight(String city1, String city2) {
    CityInfo ci1 = forecastByCity(city1);
    CityInfo ci2 = forecastByCity(city2);

    return (ci1.getDaylightHours() >= ci2.getDaylightHours()) ? ci1 : ci2;
  }
}
