package com.kanterroman.weatherntfy.clients;

import com.kanterroman.weatherntfy.clients.dto.WeatherForecast;
import com.kanterroman.weatherntfy.config.WeatherAPIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Controller
public class WeatherAPIClient {
    private final WebClient webClient;
    private final WeatherAPIConfig weatherAPIConfig;

    @Autowired
    public WeatherAPIClient(WeatherAPIConfig weatherAPIConfig) {
        webClient = WebClient
                .builder()
                .baseUrl("https://api.weatherapi.com/v1")
                .build();
        this.weatherAPIConfig = weatherAPIConfig;
    }

    public Mono<WeatherForecast> getWeatherForecast(String city) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast.json")
                        .queryParam("key", weatherAPIConfig.getApiKey())
                        .queryParam("q", city)
                        .queryParam("lang", "ru")
                        .build())
                .retrieve()
                .bodyToMono(WeatherForecast.class);
    }
}
