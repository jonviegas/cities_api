package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.builder.CityBuilder;
import com.jonservices.citiesapi.model.City;
import com.jonservices.citiesapi.repositories.CityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.jonservices.citiesapi.utils.JSONConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    private static Pageable DEFAULT_PAGEABLE;
    private static String CITY_API_URL_PATH;
    private static City EXPECTED_CITY;
    private static City ANOTHER_CITY;
    private static Page<City> ALL_CITIES_PAGE;
    private static Page<City> CITIES_PAGE;
    private static Page<City> EMPTY_CITIES_PAGE;
    private static long VALID_CITY_ID;
    private static long INVALID_CITY_ID;
    private static String VALID_CITY_NAME;
    private static String INVALID_CITY_NAME;
    private static String VALID_STATE_UF;
    private static String INVALID_STATE_UF;
    private static BigDecimal EXPECTED_DISTANCE_BY_POINTS;
    private static BigDecimal EXPECTED_DISTANCE_BY_CUBE;

    private MockMvc mockMvc;

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityController cityController;

    @BeforeAll
    static void setupAll() {
        DEFAULT_PAGEABLE = PageRequest.ofSize(20);
        CITY_API_URL_PATH = "/cities";
        EXPECTED_CITY = CityBuilder.builder().build().toCity();
        ANOTHER_CITY = CityBuilder.builder().build().aDifferentOne();
        ALL_CITIES_PAGE = new PageImpl<>(Arrays.asList(EXPECTED_CITY, ANOTHER_CITY));
        CITIES_PAGE = new PageImpl<>(Collections.singletonList(EXPECTED_CITY));
        EMPTY_CITIES_PAGE = new PageImpl<>(new ArrayList<>());
        VALID_CITY_ID = 3658L;
        INVALID_CITY_ID = 0L;
        VALID_CITY_NAME = "Rio de Janeiro";
        INVALID_CITY_NAME = "Gotham";
        VALID_STATE_UF = "RJ";
        INVALID_STATE_UF = "LA";
        EXPECTED_DISTANCE_BY_POINTS = BigDecimal.valueOf(6.860284848909248);
        EXPECTED_DISTANCE_BY_CUBE = BigDecimal.valueOf(10472.238251167762);
    }

    @BeforeEach
    void setupEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(cityController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test // GET Find All
    @DisplayName("When GET is called then all cities and ok status is returned")
    void whenGETIsCalledThenAllCitiesAndOkStatusIsReturned() throws Exception {
        // when
        when(cityRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(ALL_CITIES_PAGE);

        // then
        mockMvc.perform(get(CITY_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ALL_CITIES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_CITY.getName())))
                .andExpect(jsonPath("$.content[0].ibge", is(EXPECTED_CITY.getIbge())))
                .andExpect(jsonPath("$.content[1].name", is(ANOTHER_CITY.getName())))
                .andExpect(jsonPath("$.content[1].ibge", is(ANOTHER_CITY.getIbge())));
    }

    @Test // GET Find by id
    @DisplayName("When GET is called with valid id then city and ok status is returned")
    void whenGETIsCalledWithValidIdThenCityAndOkStatusIsReturned() throws Exception {
        // when
        when(cityRepository.findById(VALID_CITY_ID)).thenReturn(Optional.of(EXPECTED_CITY));

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/" + VALID_CITY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(asJsonString(EXPECTED_CITY)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(EXPECTED_CITY.getName())));
    }

    @Test // GET Find By Name
    @DisplayName("When GET is called with valid name then city list and ok status is returned")
    void whenGETIsCalledWithValidNameThenCityListAndOkStatusIsReturned() throws Exception {
        // when
        when(cityRepository.findByNameContainingIgnoreCase(VALID_CITY_NAME, DEFAULT_PAGEABLE)).thenReturn(CITIES_PAGE);

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/search/" + VALID_CITY_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(CITIES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_CITY.getName())));
    }

    @Test // GET Find By State
    @DisplayName("When GET is called with valid state uf then city list and ok status is returned")
    void whenGETIsCalledWithValidStateUfThenCityListAndOkStatusIsReturned() throws Exception {
        // when
        when(cityRepository.findByState(VALID_STATE_UF, DEFAULT_PAGEABLE)).thenReturn(CITIES_PAGE);

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/search/state/" + VALID_STATE_UF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(CITIES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_CITY.getName())));
    }

    @Test // GET Distance By Points
    @DisplayName("When GET is called then return distance by points between two cities")
    void whenGETIsCalledThenReturnDistanceByPointsBetweenTwoCities() throws Exception {
        // when

        // find by id
        final long cityId1 = EXPECTED_CITY.getId();
        final long cityId2 = ANOTHER_CITY.getId();
        when(cityRepository.findById(cityId1)).thenReturn(Optional.of(EXPECTED_CITY));
        when(cityRepository.findById(cityId2)).thenReturn(Optional.of(ANOTHER_CITY));

        // calculate distance
        when(cityRepository.distanceByPoints(cityId1, cityId2)).thenReturn(EXPECTED_DISTANCE_BY_POINTS);

        // request params
        final String by = "points";
        final String requestParams = "?by=" + by + "&from=" + cityId1 + "&to=" + cityId2;

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/distance" + requestParams)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_DISTANCE_BY_POINTS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(EXPECTED_DISTANCE_BY_POINTS.doubleValue())));
    }

    @Test // GET Distance By Cube
    @DisplayName("When GET is called then return distance by cube between two cities")
    void whenGETIsCalledThenReturnDistanceByCubeBetweenTwoCities() throws Exception {
        // when

        // find by id
        final long cityId1 = EXPECTED_CITY.getId();
        final long cityId2 = ANOTHER_CITY.getId();
        when(cityRepository.findById(cityId1)).thenReturn(Optional.of(EXPECTED_CITY));
        when(cityRepository.findById(cityId2)).thenReturn(Optional.of(ANOTHER_CITY));

        // calculate distance
        final BigDecimal cityLat1 = EXPECTED_CITY.getLatitude();
        final BigDecimal cityLon1 = EXPECTED_CITY.getLongitude();
        final BigDecimal cityLat2 = ANOTHER_CITY.getLatitude();
        final BigDecimal cityLon2 = ANOTHER_CITY.getLongitude();
        when(cityRepository.distanceByCube(cityLat1, cityLon1, cityLat2, cityLon2)).thenReturn(EXPECTED_DISTANCE_BY_CUBE);

        // request params
        final String by = "cube";
        final String requestParams = "?by=" + by + "&from=" + cityId1 + "&to=" + cityId2;

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/distance" + requestParams)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_DISTANCE_BY_POINTS)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(EXPECTED_DISTANCE_BY_CUBE.doubleValue())));
    }

    @Test // GET Find by id ResourceNotFoundException
    @DisplayName("When GET is called with an invalid city id then it should thrown an error")
    void whenGETIsCalledWithAnInvalidCityIdThenItShouldThrownAnError() throws Exception {
        // when
        when(cityRepository.findById(INVALID_CITY_ID)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/" + INVALID_CITY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Find by Name ResourceNotFoundException
    @DisplayName("When GET is called with an invalid name then it should thrown an error")
    void whenGETIsCalledWithAnInvalidNameThenItShouldThrownAnError() throws Exception {
        // when
        when(cityRepository.findByNameContainingIgnoreCase(INVALID_CITY_NAME, DEFAULT_PAGEABLE)).thenReturn(EMPTY_CITIES_PAGE);

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/search/" + INVALID_CITY_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Find by State ResourceNotFoundException
    @DisplayName("When GET is called with an invalid state uf then it should thrown an error")
    void whenGETIsCalledWithAnInvalidStateUfThenItShouldThrownAnError() throws Exception {
        // when
        when(cityRepository.findByState(INVALID_STATE_UF, DEFAULT_PAGEABLE)).thenReturn(EMPTY_CITIES_PAGE);

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/search/state/" + INVALID_STATE_UF)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Distance InvalidDistanceCalculationException
    @DisplayName("When GET is called with an invalid distance type then it should thrown an error")
    void whenGETIsCalledWithAnInvalidDistanceTypeThenItShouldThrownAnError() throws Exception {
        // when

        // find by id
        final long cityId1 = EXPECTED_CITY.getId();
        final long cityId2 = ANOTHER_CITY.getId();
        when(cityRepository.findById(cityId1)).thenReturn(Optional.of(EXPECTED_CITY));
        when(cityRepository.findById(cityId2)).thenReturn(Optional.of(ANOTHER_CITY));

        // request params
        final String by = "km"; // invalid
        final String requestParams = "?by=" + by + "&from=" + cityId1 + "&to=" + cityId2;

        // then
        mockMvc.perform(get(CITY_API_URL_PATH + "/distance" + requestParams)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_DISTANCE_BY_POINTS)))
                .andExpect(status().isBadRequest());
    }
}
