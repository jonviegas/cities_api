package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.model.Country;
import com.jonservices.citiesapi.repositories.CountryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/countries")
@Tag(name = "Country Controller")
public class CountryController extends AbstractController<Country> {

    public CountryController(CountryRepository countryRepository) {
        super(countryRepository);
    }
}
