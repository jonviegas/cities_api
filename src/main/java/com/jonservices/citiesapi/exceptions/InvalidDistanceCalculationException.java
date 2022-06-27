package com.jonservices.citiesapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDistanceCalculationException extends RuntimeException {
    public InvalidDistanceCalculationException(String by) {
        super("Can't calculate distance by " + by);
    }
}
