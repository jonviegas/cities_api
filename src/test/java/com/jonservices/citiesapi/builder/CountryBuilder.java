package com.jonservices.citiesapi.builder;

import com.jonservices.citiesapi.model.Country;
import lombok.Builder;

@Builder
public class CountryBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Brazil";

    @Builder.Default
    private String code = "BR";

    @Builder.Default
    private Integer bacen = 1058;

    public Country toCountry() {
        return new Country(id, name, code, bacen);
    }

    public Country aDifferentOne() {
        return new Country(11L, "Argentina", "AR", 639);
    }
}
