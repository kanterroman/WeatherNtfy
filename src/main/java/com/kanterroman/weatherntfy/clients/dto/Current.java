package com.kanterroman.weatherntfy.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Current(
        @JsonProperty("temp_c") Double tempCel,
        @JsonProperty("condition") Condition condition
) {
}
