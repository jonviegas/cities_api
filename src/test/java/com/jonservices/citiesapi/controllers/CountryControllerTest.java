package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.builder.CountryBuilder;
import com.jonservices.citiesapi.model.Country;
import com.jonservices.citiesapi.repositories.CountryRepository;
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
class CountryControllerTest {

    private static Pageable DEFAULT_PAGEABLE;
    private static String COUNTRY_API_URL_PATH;
    private static Country EXPECTED_COUNTRY;
    private static Country ANOTHER_COUNTRY;
    private static Page<Country> ALL_COUNTRIES_PAGE;
    private static Page<Country> FIND_BY_NAME_COUNTRIES_PAGE;
    private static Page<Country> EMPTY_COUNTRIES_PAGE;
    private static long VALID_COUNTRY_ID;
    private static long INVALID_COUNTRY_ID;
    private static String VALID_COUNTRY_NAME;
    private static String INVALID_COUNTRY_NAME;

    private MockMvc mockMvc;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryController countryController;

    @BeforeAll
    static void setupAll() {
        DEFAULT_PAGEABLE = PageRequest.ofSize(20);
        COUNTRY_API_URL_PATH = "/countries";
        EXPECTED_COUNTRY = CountryBuilder.builder().build().toCountry();
        ANOTHER_COUNTRY = CountryBuilder.builder().build().aDifferentOne();
        ALL_COUNTRIES_PAGE = new PageImpl<>(Arrays.asList(EXPECTED_COUNTRY, ANOTHER_COUNTRY));
        FIND_BY_NAME_COUNTRIES_PAGE = new PageImpl<>(Collections.singletonList(EXPECTED_COUNTRY));
        EMPTY_COUNTRIES_PAGE = new PageImpl<>(new ArrayList<>());
        VALID_COUNTRY_ID = 1L;
        INVALID_COUNTRY_ID = 0L;
        VALID_COUNTRY_NAME = EXPECTED_COUNTRY.getName();
        INVALID_COUNTRY_NAME = "Narnia";
    }

    @BeforeEach
    void setupEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(countryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test // GET Find All
    @DisplayName("When GET is called then all countries and ok status is returned")
    void whenGETIsCalledThenAllCountriesAndOkStatusIsReturned() throws Exception {
        // when
        when(countryRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(ALL_COUNTRIES_PAGE);

        // then
        mockMvc.perform(get(COUNTRY_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ALL_COUNTRIES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_COUNTRY.getName())))
                .andExpect(jsonPath("$.content[0].bacen", is(EXPECTED_COUNTRY.getBacen())))
                .andExpect(jsonPath("$.content[1].name", is(ANOTHER_COUNTRY.getName())))
                .andExpect(jsonPath("$.content[1].bacen", is(ANOTHER_COUNTRY.getBacen())));
    }

    @Test // GET Find by id
    @DisplayName("When GET is called with valid id then country and ok status is returned")
    void whenGETIsCalledWithValidIdThenCountryAndOkStatusIsReturned() throws Exception {
        // when
        when(countryRepository.findById(VALID_COUNTRY_ID)).thenReturn(Optional.of(EXPECTED_COUNTRY));

        // then
        mockMvc.perform(get(COUNTRY_API_URL_PATH + "/" + VALID_COUNTRY_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(asJsonString(EXPECTED_COUNTRY)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(EXPECTED_COUNTRY.getName())));
    }

    @Test // GET Find By Name
    @DisplayName("When GET is called with valid name then country list and ok status is returned")
    void whenGETIsCalledWithValidNameThenCountryListAndOkStatusIsReturned() throws Exception {
        // when
        when(countryRepository.findByNameContainingIgnoreCase(VALID_COUNTRY_NAME, DEFAULT_PAGEABLE)).thenReturn(FIND_BY_NAME_COUNTRIES_PAGE);

        // then
        mockMvc.perform(get(COUNTRY_API_URL_PATH + "/search/" + VALID_COUNTRY_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(FIND_BY_NAME_COUNTRIES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_COUNTRY.getName())));
    }

    @Test // GET Find by id ResourceNotFoundException
    @DisplayName("When GET is called with an invalid country id then it should thrown an error")
    void whenGETIsCalledWithAnInvalidCountryIdThenItShouldThrownAnError() throws Exception {
        // when
        when(countryRepository.findById(INVALID_COUNTRY_ID)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(COUNTRY_API_URL_PATH + "/" + INVALID_COUNTRY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Find by Name ResourceNotFoundException
    @DisplayName("When GET is called with an invalid name then it should thrown an error")
    void whenGETIsCalledWithAnInvalidNameThenItShouldThrownAnError() throws Exception {
        // when
        when(countryRepository.findByNameContainingIgnoreCase(INVALID_COUNTRY_NAME, DEFAULT_PAGEABLE)).thenReturn(EMPTY_COUNTRIES_PAGE);

        // then
        mockMvc.perform(get(COUNTRY_API_URL_PATH + "/search/" + INVALID_COUNTRY_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
