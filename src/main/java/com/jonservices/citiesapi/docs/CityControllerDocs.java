package com.jonservices.citiesapi.docs;

import com.jonservices.citiesapi.model.City;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public interface CityControllerDocs {

    @Operation(summary = "Returns a list of cities from a specific state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status if records exist"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when state does not exist")
    })
    Page<City> findByState(@PathVariable String name, Pageable page);

    @Operation(summary = "Calculates the distance between two cities by points or by cube")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status when the calculation is successful"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST status when an invalid calculation type is passed"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when a city does not exists with a specific id")
    })
    BigDecimal getDistance(@RequestParam(required = false, name = "by", defaultValue = "points") String by,
                           @RequestParam(name = "from") Long cityId1,
                           @RequestParam(name = "to") Long cityId2);

}
