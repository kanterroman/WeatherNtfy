package com.kanterroman.weatherntfy.controller;

import com.kanterroman.weatherntfy.clients.WeatherAPIClient;
import com.kanterroman.weatherntfy.config.BotConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
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
        setBotCommands();
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
                switch (update.getMessage().getText().split(" ")[0]) {
                    case "/start", "/help" -> help(update);
                    case  "/forecast" -> forecast(update);
                    case null, default -> unrecognized(update);
                }
            } catch (TelegramApiException e) {
                log.error("Error when answering to user: {}", e.getMessage());
            } catch (WebClientResponseException e) {
                log.error("Error when requesting to weather api: {}", e.getMessage());
            }
        }
    }

    private void help(Update update) throws TelegramApiException {
        SendMessage message = SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text("Привет! Я бот, который предназначен для демонстрации текущей погоды!" +
                        "Напиши /forecast город, чтобы получить прогноз на сегодня!")
                .build();
        telegramClient.execute(message);
    }

    private void forecast(Update update) throws TelegramApiException {
        String[] command = update.getMessage().getText().split(" ");
        String messageText;
        if (command.length < 2) {
            messageText = "Введите вместе с командой название города, для которого хотите получить прогноз!";
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < command.length; i++) {
                sb.append(command[i]);
            }
            String city = sb.toString();
            try {
                var forecastData = weatherClient.getWeatherForecast(city).block();
                if (forecastData == null) {
                    log.error("Weather API request failed: null response for city {}", city);
                    messageText = "Простите, для Вашего города не было найдено информации";
                } else {
                    log.info("Weather API request for city {}", city);
                    messageText = String.format("""
                                    Город: %s
                                    Дата: %s
                                    Температура от %sºC до %sºC, \
                                    Средняя: %sºC
                                    Ветер до %s км/ч
                                    Вероятность дождя: %s %%""",
                            city, forecastData.forecast().forecastDay().getFirst().date(),
                            forecastData.forecast().forecastDay().getFirst().day().mintempCel(),
                            forecastData.forecast().forecastDay().getFirst().day().maxtempCel(),
                            forecastData.forecast().forecastDay().getFirst().day().avgtempCel(),
                            forecastData.forecast().forecastDay().getFirst().day().maxWindKilPerHour(),
                            forecastData.forecast().forecastDay().getFirst().day().chanceOfRain());
                }
            } catch (WebClientResponseException e) {
                messageText = "Информация не найдена. Проверьте, что правильно ввели название.";
            }
        }
        SendMessage message = SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text(messageText)
                .build();
        telegramClient.execute(message);
    }

    private void unrecognized(Update update) throws TelegramApiException {
        SendMessage message = SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text("Неизвестная команда, пожалуйста, выберите одну из доступных команд.")
                .build();
        telegramClient.execute(message);
    }

    private void wrongCityPassed(Update update) throws TelegramApiException {
        SendMessage message = SendMessage
                .builder()
                .chatId(update.getMessage().getChatId())
                .text("Передайте")
                .build();
        telegramClient.execute(message);
    }

    private void setBotCommands() {
        List<BotCommand> botCommands = new ArrayList<>();
        botCommands.add(new BotCommand("start", "Запустить бота"));
        botCommands.add(new BotCommand("help", "Информация о командах бота"));
        botCommands.add(new BotCommand("forecast", "Получить прогноз погоды для указанного города за сегодняшний день"));

        try {
            telegramClient.execute(new SetMyCommands(botCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error when calling SetMyCommands: {}", e.getMessage());
        }
    }
}