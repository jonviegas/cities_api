package com.jonservices.citiesapi.docs;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface AbstractControllerDocs<T> {

    @Operation(summary = "Returns all records")
    @ApiResponse(responseCode = "200", description = "Returns OK status")
    Page<T> findAll(Pageable page);

    @Operation(summary = "Returns a record by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status if the record exists"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST status when an invalid id format is passed"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when the record does not exists")
    })
    T findById(@PathVariable Long id);

    @Operation(summary = "Returns a list of records that contains a specific name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status if records exist"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when records does not exist")
    })
    Page<T> findByName(@PathVariable String name, Pageable page);
}
