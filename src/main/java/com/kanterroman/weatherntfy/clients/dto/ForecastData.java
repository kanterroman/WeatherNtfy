package com.kanterroman.weatherntfy.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ForecastData(
        LocalDate date,
        Day day
) {

}
