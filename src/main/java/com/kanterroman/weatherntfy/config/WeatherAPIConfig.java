package com.kanterroman.weatherntfy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class WeatherAPIConfig {
    @Value("${api.key}")
    private String apiKey;
}
