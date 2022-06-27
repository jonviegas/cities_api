package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.model.State;
import com.jonservices.citiesapi.repositories.StateRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/states")
@Tag(name = "State Controller")
public class StateController extends AbstractController<State> {

    public StateController(StateRepository stateRepository) {
        super(stateRepository);
    }
}
