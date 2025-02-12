package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.exceptions.CityNotFoundException;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {

  @Autowired
  VisualcrossingRepository weatherRepo;

  /**
   * Retrieves the weather forecast for a given city by querying the weather repository.
   * If the API returns a HttpStatus.BAD_REQUEST error, indicating an invalid city name,
   * this method throws a CityNotFoundException with the provided city name.
   * For all other API errors, the exception is rethrown and handled by the global exception handler.
   *
   * @param city the name of the city for which the weather forecast is requested
   * @return a {@link CityInfo} object containing weather details for the specified city
   * @throws CityNotFoundException if the city name is invalid
   * @throws HttpClientErrorException for other API-related errors
   */
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

  /**
   * Compares the daylight duration of two cities and returns the city with the longer daylight period.
   * This method retrieves the weather forecast for both cities and calculates their daylight duration
   * in minutes. It then returns the CityInfo object of the city with the greater daylight duration.
   * If both cities have the same daylight duration, the first city is returned.
   *
   * @param city1 the name of the first city to compare
   * @param city2 the name of the second city to compare
   * @return the {@link CityInfo} object of the city with the longer daylight duration
   * @throws CityNotFoundException if either city name is invalid
   * @throws HttpClientErrorException for other API-related errors
   */
  public CityInfo compareDaylight(String city1, String city2) {
    CityInfo ci1 = forecastByCity(city1);
    CityInfo ci2 = forecastByCity(city2);

    return (ci1.getDaylightMinutes() >= ci2.getDaylightMinutes()) ? ci1 : ci2;
  }

  /**
   * Determines which of the two specified cities are currently experiencing rain.
   *
   * @param city1 the name of the first city to check
   * @param city2 the name of the second city to check
   * @return a list of {@link CityInfo} objects for cities where it is currently raining;
   *         an empty list if neither city is experiencing rain.
   * @throws CityNotFoundException if either city name is invalid or not found.
   * @throws HttpClientErrorException for other API-related errors
   */
  public List<CityInfo> getRainingCities(String city1, String city2) {
    CityInfo ci1 = forecastByCity(city1);
    CityInfo ci2 = forecastByCity(city2);

    List<CityInfo> rainingCities = new ArrayList<>();

    if (ci1.isRaining()) { rainingCities.add(ci1); }
    if (ci2.isRaining()) { rainingCities.add(ci2); }
    return rainingCities;
  }
}
