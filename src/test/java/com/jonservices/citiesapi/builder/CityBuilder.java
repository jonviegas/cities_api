package com.jonservices.citiesapi.builder;

import com.jonservices.citiesapi.model.City;
import com.jonservices.citiesapi.model.State;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class CityBuilder {

    @Builder.Default
    private Long id = 3658L;

    @Builder.Default
    private String name = "Rio de Janeiro";

    @Builder.Default
    private State state = StateBuilder.builder().build().toState();

    @Builder.Default
    private Integer ibge = 3304557;

    @Builder.Default
    private String geolocation = "(-22.9129009246826,-43.2002983093262)";

    @Builder.Default
    private BigDecimal latitude = BigDecimal.valueOf(-22.9129009246826);

    @Builder.Default
    private BigDecimal longitude = BigDecimal.valueOf(-43.2002983093262);

    public City toCity() {
        return new City(id, name, state, ibge, geolocation, latitude, longitude);
    }

    public City aDifferentOne() {
        return new City(
                3638L,
                "Niterói",
                state,
                3303302,
                "(-22.8831996917725,-43.103401184082)",
                BigDecimal.valueOf(-22.8831996917725),
                BigDecimal.valueOf(-43.103401184082)
        );
    }
}
