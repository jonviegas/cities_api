package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.builder.StateBuilder;
import com.jonservices.citiesapi.model.State;
import com.jonservices.citiesapi.repositories.StateRepository;
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
class StateControllerTest {

    private static Pageable DEFAULT_PAGEABLE;
    private static String STATE_API_URL_PATH;
    private static State EXPECTED_STATE;
    private static State ANOTHER_STATE;
    private static Page<State> ALL_STATES_PAGE;
    private static Page<State> FIND_BY_NAME_STATES_PAGE;
    private static Page<State> EMPTY_STATES_PAGE;
    private static long VALID_STATE_ID;
    private static long INVALID_STATE_ID;
    private static String VALID_STATE_NAME;
    private static String INVALID_STATE_NAME;

    private MockMvc mockMvc;

    @Mock
    private StateRepository stateRepository;

    @InjectMocks
    private StateController stateController;

    @BeforeAll
    static void setupAll() {
        DEFAULT_PAGEABLE = PageRequest.ofSize(20);
        STATE_API_URL_PATH = "/states";
        EXPECTED_STATE = StateBuilder.builder().build().toState();
        ANOTHER_STATE = StateBuilder.builder().build().aDifferentOne();
        ALL_STATES_PAGE = new PageImpl<>(Arrays.asList(EXPECTED_STATE, ANOTHER_STATE));
        FIND_BY_NAME_STATES_PAGE = new PageImpl<>(Collections.singletonList(EXPECTED_STATE));
        EMPTY_STATES_PAGE = new PageImpl<>(new ArrayList<>());
        VALID_STATE_ID = 1L;
        INVALID_STATE_ID = 0L;
        VALID_STATE_NAME = EXPECTED_STATE.getName();
        INVALID_STATE_NAME = "Tatooine";
    }

    @BeforeEach
    void setupEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(stateController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test // GET Find All
    @DisplayName("When GET is called then all states and ok status is returned")
    void whenGETIsCalledThenAllStatesAndOkStatusIsReturned() throws Exception {
        // when
        when(stateRepository.findAll(DEFAULT_PAGEABLE)).thenReturn(ALL_STATES_PAGE);

        // then
        mockMvc.perform(get(STATE_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ALL_STATES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_STATE.getName())))
                .andExpect(jsonPath("$.content[0].uf", is(EXPECTED_STATE.getUf())))
                .andExpect(jsonPath("$.content[1].name", is(ANOTHER_STATE.getName())))
                .andExpect(jsonPath("$.content[1].uf", is(ANOTHER_STATE.getUf())));
    }

    @Test // GET Find by id
    @DisplayName("When GET is called with valid id then state and ok status is returned")
    void whenGETIsCalledWithValidIdThenStateAndOkStatusIsReturned() throws Exception {
        // when
        when(stateRepository.findById(VALID_STATE_ID)).thenReturn(Optional.of(EXPECTED_STATE));

        // then
        mockMvc.perform(get(STATE_API_URL_PATH + "/" + VALID_STATE_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(asJsonString(EXPECTED_STATE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(EXPECTED_STATE.getName())));
    }

    @Test // GET Find By Name
    @DisplayName("When GET is called with valid name then state list and ok status is returned")
    void whenGETIsCalledWithValidNameThenStateListAndOkStatusIsReturned() throws Exception {
        // when
        when(stateRepository.findByNameContainingIgnoreCase(VALID_STATE_NAME, DEFAULT_PAGEABLE)).thenReturn(FIND_BY_NAME_STATES_PAGE);

        // then
        mockMvc.perform(get(STATE_API_URL_PATH + "/search/" + VALID_STATE_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(FIND_BY_NAME_STATES_PAGE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is(EXPECTED_STATE.getName())));
    }

    @Test // GET Find by id ResourceNotFoundException
    @DisplayName("When GET is called with an invalid state id then it should thrown an error")
    void whenGETIsCalledWithAnInvalidStateIdThenItShouldThrownAnError() throws Exception {
        // when
        when(stateRepository.findById(INVALID_STATE_ID)).thenReturn(Optional.empty());

        // then
        mockMvc.perform(get(STATE_API_URL_PATH + "/" + INVALID_STATE_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Find by Name ResourceNotFoundException
    @DisplayName("When GET is called with an invalid name then it should thrown an error")
    void whenGETIsCalledWithAnInvalidNameThenItShouldThrownAnError() throws Exception {
        // when
        when(stateRepository.findByNameContainingIgnoreCase(INVALID_STATE_NAME, DEFAULT_PAGEABLE)).thenReturn(EMPTY_STATES_PAGE);

        // then
        mockMvc.perform(get(STATE_API_URL_PATH + "/search/" + INVALID_STATE_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
