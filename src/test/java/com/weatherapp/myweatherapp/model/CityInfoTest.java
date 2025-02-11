package com.weatherapp.myweatherapp.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class CityInfoTest {
    private CityInfo cityInfo;

    @BeforeEach
    void setUp() {
        cityInfo = new CityInfo();
        cityInfo.currentConditions = new CityInfo.CurrentConditions();
    }


    // getDaylightMinutes() unit tests
    //
    //
    @Test
    void testGetDaylightHours_validTimeRange() {
        cityInfo.currentConditions.sunrise = "06:00";
        cityInfo.currentConditions.sunset = "18:00";

        long expectedMinutes = Duration.between(java.time.LocalTime.parse("06:00"), java.time.LocalTime.parse("18:00")).toMinutes();
        long actualMinutes = cityInfo.getDaylightMinutes();

        assertEquals(expectedMinutes, actualMinutes);
    }

    @Test
    void testGetDaylightHours_edgeCaseShortDay() {
        cityInfo.currentConditions.sunrise = "08:30";
        cityInfo.currentConditions.sunset = "15:45";

        long expectedMinutes = Duration.between(java.time.LocalTime.parse("08:30"), java.time.LocalTime.parse("15:45")).toMinutes();
        long actualMinutes = cityInfo.getDaylightMinutes();

        assertEquals(expectedMinutes, actualMinutes);
    }

    @Test
    void testGetDaylightHours_midnightSun() {
        cityInfo.currentConditions.sunrise = "00:00";
        cityInfo.currentConditions.sunset = "23:59";

        long expectedMinutes = Duration.between(java.time.LocalTime.parse("00:00"), java.time.LocalTime.parse("23:59")).toMinutes();
        long actualMinutes = cityInfo.getDaylightMinutes();

        assertEquals(expectedMinutes, actualMinutes);
    }

    @Test
    void testGetDaylightHours_invalidFormat_throwsException() {
        cityInfo.currentConditions.sunrise = "6 AM";
        cityInfo.currentConditions.sunset = "6 PM";

        assertThrows(java.time.format.DateTimeParseException.class, cityInfo::getDaylightMinutes);
    }

    @Test
    void testGetDaylightHours_nullConditions() {
        cityInfo.currentConditions.sunrise = null;
        cityInfo.currentConditions.sunset = null;

        long actualMinutes = cityInfo.getDaylightMinutes();

        assertEquals(0, actualMinutes);
    }

    // isRaining() unit tests
    //
    //
    @Test
    void testIsRaining_validConditions() {
        cityInfo.currentConditions.conditions = "Rain, Partially cloudy";
        assertTrue(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_invalidConditions() {
        cityInfo.currentConditions.conditions = "Overcast";
        assertFalse(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_emptyConditions() {
        cityInfo.currentConditions.conditions = "";
        assertFalse(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_nullConditions() {
        cityInfo.currentConditions.conditions = null;
        assertFalse(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_rainAtStart() {
        cityInfo.currentConditions.conditions = "Rain, Cloudy, Windy";
        assertTrue(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_rainAtMiddle() {
        cityInfo.currentConditions.conditions = "Cloudy, Rain, Windy";
        assertTrue(cityInfo.isRaining());
    }

    @Test
    void testIsRaining_rainAtEnd() {
        cityInfo.currentConditions.conditions = "Cloudy, Windy, Rain";
        assertTrue(cityInfo.isRaining());
    }
}
