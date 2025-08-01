package com.kanterroman.weatherntfy.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Forecast(
        @JsonProperty("forecastday") List<ForecastData> forecastDay
        ) {
}
