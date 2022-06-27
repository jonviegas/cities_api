package com.jonservices.citiesapi.hateoas;

import com.jonservices.citiesapi.controllers.CityController;
import com.jonservices.citiesapi.controllers.CountryController;
import com.jonservices.citiesapi.controllers.StateController;
import com.jonservices.citiesapi.model.City;
import com.jonservices.citiesapi.model.Country;
import com.jonservices.citiesapi.model.State;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.RepresentationModel;

import static com.jonservices.citiesapi.utils.GenericsUtils.getInstanceClassName;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class HateoasImpl <T extends RepresentationModel<T>> {

    public static <T> void addLinkToAll(T region) {
        final PageRequest page = PageRequest.ofSize(10);
        switch (getInstanceClassName(region)) {
            case "Country":
                final Country country = ((Country) region);
                country.add(linkTo(methodOn(CountryController.class).findAll(page)).withRel("allCountries"));
                break;
            case "State":
                final State state = ((State) region);
                state.add(linkTo(methodOn(StateController.class).findAll(page)).withRel("allStates"));
                addLinkToItself(state.getCountry());
                break;
            case "City":
                final City city = ((City) region);
                city.add(linkTo(methodOn(CityController.class).findAll(page)).withRel("allCities"));
                addLinkToItself(city.getState());
                break;
        }
    }

    public static <T> void addLinkToItself(T region) {
        switch (getInstanceClassName(region)) {
            case "Country":
                final Country country = ((Country) region);
                if (!country.hasLinks())
                    country.add(linkTo(methodOn(CountryController.class).findById(country.getId())).withSelfRel());
                break;
            case "State":
                final State state = ((State) region);
                if (!state.hasLinks())
                    state.add(linkTo(methodOn(StateController.class).findById(state.getId())).withSelfRel());
                if (state.getCountry() != null)
                    addLinkToItself(state.getCountry());
                break;
            case "City":
                final City city = ((City) region);
                if (!city.hasLinks())
                    city.add(linkTo(methodOn(CityController.class).findById(city.getId())).withSelfRel());
                addLinkToItself(city.getState());
                break;
        }
    }

}
