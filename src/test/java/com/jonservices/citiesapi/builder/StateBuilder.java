package com.jonservices.citiesapi.builder;

import com.jonservices.citiesapi.model.Country;
import com.jonservices.citiesapi.model.State;
import lombok.Builder;

import java.util.List;

@Builder
public class StateBuilder {

    @Builder.Default
    private Long id = 19L;

    @Builder.Default
    private String name = "Rio de Janeiro";

    @Builder.Default
    private String uf = "RJ";

    @Builder.Default
    private Country country = CountryBuilder.builder().build().toCountry();

    @Builder.Default
    private List<Integer> ddd = List.of(24, 22, 21);

    public State toState() {
        return new State(id, name, uf, country, ddd);
    }

    public State aDifferentOne() {
        return new State(5L, "Bahia", "BA", country, List.of(77, 75, 73, 74, 71));
    }
}
