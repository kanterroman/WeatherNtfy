package com.kanterroman.weatherntfy.clients;

import com.kanterroman.weatherntfy.config.WeatherAPIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherAPIClient {
    private final WebClient webClient;
    private final WeatherAPIConfig weatherAPIConfig;

    @Autowired
    public WeatherAPIClient(WeatherAPIConfig weatherAPIConfig) {
        webClient = WebClient
                .builder()
                .baseUrl("http://api.weatherapi.com/v1")
                .build();
        this.weatherAPIConfig = weatherAPIConfig;
    }

    public Mono<String> getWeatherForecast(String city) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast.json")
                        .queryParam("key", weatherAPIConfig.getApiKey())
                        .queryParam("q", city)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
