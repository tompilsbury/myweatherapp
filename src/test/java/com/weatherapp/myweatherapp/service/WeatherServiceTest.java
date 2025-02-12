package com.weatherapp.myweatherapp.service;

import com.weatherapp.myweatherapp.exceptions.CityNotFoundException;
import com.weatherapp.myweatherapp.model.CityInfo;
import com.weatherapp.myweatherapp.repository.VisualcrossingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

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
        when(city1.getDaylightMinutes()).thenReturn(720L); // 12 hours
        when(city2.getDaylightMinutes()).thenReturn(600L); // 10 hours

        CityInfo result = weatherService.compareDaylight("New York", "Los Angeles");
        assertEquals(city1, result);
    }

    @Test
    void testCompareDaylight_city2HasMoreDaylight() {
        when(weatherRepo.getByCity("Chicago")).thenReturn(city1);
        when(weatherRepo.getByCity("Miami")).thenReturn(city2);
        when(city1.getDaylightMinutes()).thenReturn(500L); // 8 hours 20 min
        when(city2.getDaylightMinutes()).thenReturn(700L); // 11 hours 40 min

        CityInfo result = weatherService.compareDaylight("Chicago", "Miami");
        assertEquals(city2, result);
    }

    @Test
    void testCompareDaylight_equalDaylight() {
        when(weatherRepo.getByCity("Seattle")).thenReturn(city1);
        when(weatherRepo.getByCity("San Francisco")).thenReturn(city2);
        when(city1.getDaylightMinutes()).thenReturn(600L); // 10 hours
        when(city2.getDaylightMinutes()).thenReturn(600L); // 10 hours

        CityInfo result = weatherService.compareDaylight("Seattle", "San Francisco");
        assertEquals(city1, result);
    }

    // isRaining() unit tests
    //
    //
    @Test
    void testGetRainingCities_city1IsRaining() {
        when(weatherRepo.getByCity("London")).thenReturn(city1);
        when(weatherRepo.getByCity("Paris")).thenReturn(city2);
        when(city1.isRaining()).thenReturn(true);
        when(city2.isRaining()).thenReturn(false);

        List<CityInfo> result = weatherService.getRainingCities("London", "Paris");
        List<CityInfo> expected = new ArrayList<>();
        expected.add(city1);
        assertEquals(result, expected);
    }

    @Test
    void testGetRainingCities_city2IsRaining() {
        when(weatherRepo.getByCity("Leicester")).thenReturn(city1);
        when(weatherRepo.getByCity("Newcastle")).thenReturn(city2);
        when(city1.isRaining()).thenReturn(false);
        when(city2.isRaining()).thenReturn(true);

        List<CityInfo> result = weatherService.getRainingCities("Leicester", "Newcastle");
        List<CityInfo> expected = new ArrayList<>();
        expected.add(city2);
        assertEquals(result, expected);
    }

    @Test
    void testGetRainingCities_bothRaining() {
        when(weatherRepo.getByCity("Milan")).thenReturn(city1);
        when(weatherRepo.getByCity("Barcelona")).thenReturn(city2);
        when(city1.isRaining()).thenReturn(true);
        when(city2.isRaining()).thenReturn(true);

        List<CityInfo> result = weatherService.getRainingCities("Milan", "Barcelona");
        List<CityInfo> expected = new ArrayList<>();
        expected.add(city1);
        expected.add(city2);
        assertEquals(result, expected);
    }

    @Test
    void testGetRainingCities_neitherRaining() {
        when(weatherRepo.getByCity("Tokyo")).thenReturn(city1);
        when(weatherRepo.getByCity("Sydney")).thenReturn(city2);
        when(city1.isRaining()).thenReturn(false);
        when(city2.isRaining()).thenReturn(false);

        List<CityInfo> result = weatherService.getRainingCities("Tokyo", "Sydney");
        List<CityInfo> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    @Test
    void testGetRainingCities_city1HasNoData() {
        when(weatherRepo.getByCity("Berlin")).thenReturn(null);
        when(weatherRepo.getByCity("Madrid")).thenReturn(city2);
        when(city2.isRaining()).thenReturn(true);

        List<CityInfo> result = weatherService.getRainingCities("Berlin", "Madrid");
        List<CityInfo> expected = new ArrayList<>();
        expected.add(city2);
        assertEquals(result, expected);
    }

    @Test
    void testGetRainingCities_invalidCity1() {
        when(weatherRepo.getByCity("FakeCity")).thenThrow(new CityNotFoundException("FakeCity"));

        assertThrows(CityNotFoundException.class, () -> weatherService.getRainingCities("FakeCity", "Berlin"));
    }

}