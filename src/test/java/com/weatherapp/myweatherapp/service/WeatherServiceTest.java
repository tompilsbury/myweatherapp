package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class WeatherServiceTest {

  @Mock
  private VisualcrossingRepository weatherRepo;

    @InjectMocks
    private WeatherService weatherService;

    private CityInfo city1;
    private CityInfo city2;
    @BeforeEach
    void setUp() {
        city1 = mock(CityInfo.class);
        city2 = mock(CityInfo.class);
    }

    @Test
    void testCompareDaylight_city1HasMoreDaylight() {
        when(weatherRepo.getByCity("New York")).thenReturn(city1);
        when(weatherRepo.getByCity("Los Angeles")).thenReturn(city2);
        when(city1.getDaylightHours()).thenReturn(720L); // 12 hours
        when(city2.getDaylightHours()).thenReturn(600L); // 10 hours

        CityInfo result = weatherService.compareDaylight("New York", "Los Angeles");
        assertEquals(city1, result);
    }

    @Test
    void testCompareDaylight_city2HasMoreDaylight() {
        when(weatherRepo.getByCity("Chicago")).thenReturn(city1);
        when(weatherRepo.getByCity("Miami")).thenReturn(city2);
        when(city1.getDaylightHours()).thenReturn(500L); // 8 hours 20 min
        when(city2.getDaylightHours()).thenReturn(700L); // 11 hours 40 min

        CityInfo result = weatherService.compareDaylight("Chicago", "Miami");
        assertEquals(city2, result);
    }

    @Test
    void testCompareDaylight_equalDaylight() {
        when(weatherRepo.getByCity("Seattle")).thenReturn(city1);
        when(weatherRepo.getByCity("San Francisco")).thenReturn(city2);
        when(city1.getDaylightHours()).thenReturn(600L); // 10 hours
        when(city2.getDaylightHours()).thenReturn(600L); // 10 hours

        CityInfo result = weatherService.compareDaylight("Seattle", "San Francisco");
        assertEquals(city1, result);
    }
}