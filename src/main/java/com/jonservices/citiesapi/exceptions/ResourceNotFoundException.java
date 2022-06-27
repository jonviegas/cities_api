package com.jonservices.citiesapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String className, String attribute, Object value) {
        super(className + " with " + attribute + " " + value + " not found");
    }

}
