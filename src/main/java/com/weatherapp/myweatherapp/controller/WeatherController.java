package com.weatherapp.myweatherapp.controller;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WeatherController {

  @Autowired
  WeatherService weatherService;

  @GetMapping("/forecast/{city}")
  public ResponseEntity<CityInfo> forecastByCity(@PathVariable("city") String city) {

    CityInfo ci = weatherService.forecastByCity(city);

    return ResponseEntity.ok(ci);
  }

  // Given two city names, compare the length of the daylight hours and return the city with the longest day
  @GetMapping("/daylight")
  public ResponseEntity<CityInfo> daylightComparison(@RequestParam("city1") String city1,
                                                     @RequestParam("city2") String city2) {
    CityInfo cityWithMoreDaylight = weatherService.compareDaylight(city1, city2);
    return ResponseEntity.ok(cityWithMoreDaylight);
  }

  // Given two city names, check which city its currently raining in
  @GetMapping("/raining")
  public ResponseEntity<List<CityInfo>> checkRaining(@RequestParam("city1") String city1,
                                                  @RequestParam("city2") String city2) {
    List<CityInfo> rainingCities = weatherService.getRainingCities(city1, city2);
    return ResponseEntity.ok(rainingCities);
  }
}
