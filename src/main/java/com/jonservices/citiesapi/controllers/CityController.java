package com.jonservices.citiesapi.controllers;

import com.jonservices.citiesapi.docs.CityControllerDocs;
import com.jonservices.citiesapi.exceptions.InvalidDistanceCalculationException;
import com.jonservices.citiesapi.exceptions.ResourceNotFoundException;
import com.jonservices.citiesapi.hateoas.HateoasImpl;
import com.jonservices.citiesapi.model.City;
import com.jonservices.citiesapi.repositories.CityRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/cities")
@Tag(name = "City Controller")
public class CityController extends AbstractController<City> implements CityControllerDocs {

    public CityController(CityRepository cityRepository) {
        super(cityRepository);
    }

    @GetMapping("/search/state/{uf}")
    public Page<City> findByState(@PathVariable String uf, Pageable page) {
        final Page<City> countryPage = ((CityRepository) repository).findByState(uf, page);
        if (countryPage.isEmpty())
            throw new ResourceNotFoundException("State", "uf", uf);
        countryPage.forEach(HateoasImpl::addLinkToItself);
        return countryPage;
    }

    @GetMapping("/distance")
    public BigDecimal getDistance(@RequestParam(required = false, name = "by", defaultValue = "points") String by,
                                  @RequestParam(name = "from") Long cityId1,
                                  @RequestParam(name = "to") Long cityId2) {
        final City city1 = verifyIfExists(cityId1);
        final City city2 = verifyIfExists(cityId2);
        switch (by.toLowerCase()) {
            case "points":
                return ((CityRepository) repository).distanceByPoints(cityId1, cityId2);
            case "cube":
                return ((CityRepository) repository).distanceByCube(city1.getLatitude(), city1.getLongitude(), city2.getLatitude(), city2.getLongitude());
            default:
                throw new InvalidDistanceCalculationException(by);
        }
    }

}

