package com.jonservices.citiesapi.docs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(info =
@Info(title = "Cities API",
      version = "v1",
      description = "This REST API provides a search and navigation " +
                    "system for information related to countries and " +
                    "locations in Brazil. In addition, it also provides " +
                    "the functionality of calculating the distance " +
                    "between two locations by their geographic points."))
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Cities API")
                        .version("V1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));

    }

}
