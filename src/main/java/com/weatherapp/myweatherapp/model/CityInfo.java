package com.weatherapp.myweatherapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

public class CityInfo {

  @JsonProperty("address")
  String address;

  @JsonProperty("description")
  String description;

  @JsonProperty("currentConditions")
  CurrentConditions currentConditions;

  @JsonProperty("days")
  List<Days> days;

  static class CurrentConditions {
    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("sunrise")
    String sunrise;

    @JsonProperty("sunset")
    String sunset;

    @JsonProperty("feelslike")
    String feelslike;

    @JsonProperty("humidity")
    String humidity;

    @JsonProperty("conditions")
    String conditions;
  }

  static class Days {

    @JsonProperty("datetime")
    String date;

    @JsonProperty("temp")
    String currentTemperature;

    @JsonProperty("tempmax")
    String maxTemperature;

    @JsonProperty("tempmin")
    String minTemperature;

    @JsonProperty("conditions")
    String conditions;

    @JsonProperty("description")
    String description;

  }

  /**
   * Calculates the total daylight duration in minutes based on the city's sunrise and sunset times.
   * If either the sunrise or sunset time is missing, the method returns 0 to indicate
   * that daylight data is unavailable. The times are parsed as LocalTime and used to calculate
   * the duration between sunrise and sunset.
   *
   * @return the total daylight duration in minutes, or 0 if sunrise or sunset data is unavailable.
   */
  @JsonIgnore
  public long getDaylightMinutes() {
    if (currentConditions.sunrise == null || currentConditions.sunset == null) {
      return 0;
    }
    LocalTime sunrise = LocalTime.parse(currentConditions.sunrise);
    LocalTime sunset = LocalTime.parse(currentConditions.sunset);
    return Duration.between(sunrise, sunset).toMinutes();
  }

}
