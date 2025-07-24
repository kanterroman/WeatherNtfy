package com.kanterroman.weatherntfy.service;

import com.kanterroman.weatherntfy.clients.WeatherAPIClient;
import com.kanterroman.weatherntfy.config.BotConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Objects;

@Slf4j
@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @NonNull
    private final TelegramClient telegramClient;
    @NonNull
    private final BotConfig config;
    @NonNull
    private final WeatherAPIClient weatherClient;

    @Autowired
    public TelegramBot(@NonNull BotConfig config, @NonNull WeatherAPIClient weatherClient) {
        this.config = config;
        this.weatherClient = weatherClient;
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                SendMessage message = SendMessage
                        .builder()
                        .chatId(update.getMessage().getChatId())
                        .text(Objects.requireNonNull(weatherClient.getWeatherForecast("Moscow").block()).substring(0, 100))
                        .build();
                telegramClient.execute(message);
            } catch (TelegramApiException e) {
                log.error("Error when answering to user: {}", e.getMessage());
            } catch (RuntimeException e) {
                log.error("Error when requesting to weather api: {}", e.getMessage());
            }
        }
    }
}