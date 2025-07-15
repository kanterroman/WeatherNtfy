package com.kanterroman.weatherntfy.service;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

public class TelegramBotCommandStrategyCreator {
    private static final Map<String, TelegramBotCommandStrategy> commandStrategies;
    static {
        commandStrategies = new HashMap<>();
        commandStrategies.put("/start", new TelegramBotStartStrategy());
    }
    public static TelegramBotCommandStrategy create(String message) {
        return commandStrategies.getOrDefault(message, new TelegramBotUndefinedCommandStrategy());
    }
}
