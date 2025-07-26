package com.kanterroman.weatherntfy.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Day(
        @JsonProperty("maxtemp_c") Double maxtempCel,
        @JsonProperty("mintemp_c") Double mintempCel,
        @JsonProperty("avgtemp_c") Double avgtempCel,
        @JsonProperty("maxwind_kph") Double maxWindKilPerHour,
        @JsonProperty("daily_chance_of_rain") Double chanceOfRain
) {
}
