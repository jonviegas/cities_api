package com.jonservices.citiesapi.utils;

public class GenericsUtils {

    public static <T> String getInstanceClassName(T object) {
        return object.getClass().getSimpleName();
    }

    public static <T> String getParameterTypeName(T object) {
        return object.getClass().getSimpleName().replaceAll("Controller", "");
    }

}
